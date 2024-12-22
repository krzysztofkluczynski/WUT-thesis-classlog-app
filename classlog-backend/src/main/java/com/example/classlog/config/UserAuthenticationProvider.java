package com.example.classlog.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.dto.UserDto;
import com.example.classlog.repository.RoleRepository;
import com.example.classlog.service.UserService;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserAuthenticationProvider {

  @Value("${security.jwt.token.secret-key:secret-key}")
  String secretKey;

  private final UserService userService;
  private final RoleRepository roleRepository;

  @PostConstruct
  protected void init() {
    // this is to avoid having the raw secret key available in the JVM
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(UserDto user) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + 3 * 3600000); // 3 hours

    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    return JWT.create()
        .withSubject(user.getEmail())
        .withIssuedAt(now)
        .withExpiresAt(validity)
        .withClaim("name", user.getName())
        .withClaim("surname", user.getSurname())
        .withClaim("role", user.getRole().getRoleName())
        .sign(algorithm);
  }


  public Authentication validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secretKey);

      JWTVerifier verifier = JWT.require(algorithm).build();
      DecodedJWT decoded = verifier.verify(token);

      UserDto user = UserDto.builder()
          .email(decoded.getSubject())
          .name(decoded.getClaim("name").asString())
          .surname(decoded.getClaim("surname").asString())
          .role(roleRepository.getByRoleName(decoded.getClaim("role").asString()))
          .build();

      SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
          "ROLE_" + user.getRole().getRoleName());

      return new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(authority));
    } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
      throw new AppException("Token has expired", HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      throw new RuntimeException("Invalid token", e);
    }
  }

  public Authentication validateTokenStrongly(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secretKey);

      JWTVerifier verifier = JWT.require(algorithm).build();
      DecodedJWT decoded = verifier.verify(token);

      UserDto user = userService.findByEmail(decoded.getSubject());

      SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
          "ROLE_" + user.getRole().getRoleName());

      return new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(authority));
    } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
      throw new AppException("Token has expired", HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      throw new RuntimeException("Invalid token", e);
    }
  }


}