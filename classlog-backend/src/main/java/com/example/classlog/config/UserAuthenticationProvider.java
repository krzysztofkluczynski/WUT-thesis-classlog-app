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
  }

  public Authentication validateTokenStrongly(String token) {
    Algorithm algorithm = Algorithm.HMAC256(secretKey);

    JWTVerifier verifier = JWT.require(algorithm).build();
    DecodedJWT decoded = verifier.verify(token);

    UserDto user = userService.findByEmail(decoded.getSubject());

    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
        "ROLE_" + user.getRole().getRoleName());

    return new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(authority));
  }

  public UserDto refreshToken(UserDto userDto) {
    String oldToken = userDto.getToken();
    if (oldToken == null || oldToken.isEmpty()) {
      throw new AppException("Token is required", HttpStatus.BAD_REQUEST);
    }

    try {
      DecodedJWT decodedJWT = JWT.decode(oldToken);

      Algorithm algorithm = Algorithm.HMAC256(secretKey);
      algorithm.verify(decodedJWT);

      String email = decodedJWT.getSubject();
      String name = decodedJWT.getClaim("name").asString();
      String surname = decodedJWT.getClaim("surname").asString();
      String roleName = decodedJWT.getClaim("role").asString();

      UserDto existingUser = userService.findByEmail(email);
      if (existingUser == null) {
        throw new AppException("User not found", HttpStatus.NOT_FOUND);
      }

      String newToken = createToken(existingUser);
      System.out.println("new token: " + newToken);

      return UserDto.builder()
          .id(existingUser.getId())
          .name(name)
          .surname(surname)
          .email(email)
          .role(existingUser.getRole())
          .token(newToken)
          .createdAt(existingUser.getCreatedAt())
          .build();

    } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
      throw new AppException("Token has expired", HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      throw new AppException("Invalid token", HttpStatus.UNAUTHORIZED);
    }
  }


}