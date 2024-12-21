package com.example.classlog.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.Role;
import com.example.classlog.repository.RoleRepository;
import com.example.classlog.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

class UserAuthenticationProviderTest {

  private UserAuthenticationProvider authProvider;

  @Mock
  private UserService userService;

  @Mock
  private RoleRepository roleRepository;

  private String secretKey = "test-secret-key";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    authProvider = new UserAuthenticationProvider(userService, roleRepository);
    authProvider.secretKey = secretKey;
    authProvider.init(); // Simulate the @PostConstruct method
  }

  @Test
  void createToken_ShouldGenerateValidToken() {
    Role role = new Role(1L, "USER");
    UserDto user = UserDto.builder()
        .email("test@example.com")
        .name("John")
        .surname("Doe")
        .role(role)
        .build();

    String token = authProvider.createToken(user);

    Algorithm algorithm = Algorithm.HMAC256(authProvider.secretKey);
    DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);

    assertEquals(user.getEmail(), decodedJWT.getSubject());
    assertEquals(user.getName(), decodedJWT.getClaim("name").asString());
    assertEquals(user.getSurname(), decodedJWT.getClaim("surname").asString());
    assertEquals(user.getRole().getRoleName(), decodedJWT.getClaim("role").asString());
  }

  @Test
  void validateToken_ShouldReturnValidAuthentication() {
    Role role = new Role(1L, "USER");
    when(roleRepository.getByRoleName("USER")).thenReturn(role);

    UserDto user = UserDto.builder()
        .email("test@example.com")
        .name("John")
        .surname("Doe")
        .role(role)
        .build();

    String token = authProvider.createToken(user);

    Authentication authentication = authProvider.validateToken(token);

    assertNotNull(authentication);
    assertTrue(authentication.isAuthenticated());

    UserDto authenticatedUser = (UserDto) authentication.getPrincipal();
    assertEquals(user.getEmail(), authenticatedUser.getEmail());
    assertEquals("ROLE_USER", authentication.getAuthorities().iterator().next().getAuthority());
  }

  @Test
  void validateTokenStrongly_ShouldReturnValidAuthentication() {
    Role role = new Role(1L, "USER");
    UserDto user = UserDto.builder()
        .email("test@example.com")
        .name("John")
        .surname("Doe")
        .role(role)
        .build();

    when(userService.findByEmail("test@example.com")).thenReturn(user);

    String token = authProvider.createToken(user);

    Authentication authentication = authProvider.validateTokenStrongly(token);

    assertNotNull(authentication);
    assertTrue(authentication.isAuthenticated());

    UserDto authenticatedUser = (UserDto) authentication.getPrincipal();
    assertEquals(user.getEmail(), authenticatedUser.getEmail());
    assertEquals("ROLE_USER", authentication.getAuthorities().iterator().next().getAuthority());
  }
}
