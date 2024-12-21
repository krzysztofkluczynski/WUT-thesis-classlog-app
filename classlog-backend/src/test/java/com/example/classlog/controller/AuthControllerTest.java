package com.example.classlog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.classlog.config.UserAuthenticationProvider;
import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.dto.CredentialsDto;
import com.example.classlog.dto.SignUpDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.service.UserService;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class AuthControllerTest {

  @Mock
  private UserService userService;

  @Mock
  private UserAuthenticationProvider userAuthenticationProvider;

  @InjectMocks
  private AuthController authController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void loginWithValidCredentials() {
    // Given
    char[] password = {'t', 'e', 's', 't'};
    CredentialsDto credentialsDto = new CredentialsDto("username", password);
    UserDto userDto = new UserDto();
    userDto.setId(1L);
    userDto.setEmail("email@example.com");
    userDto.setToken("token");

    when(userService.login(any(CredentialsDto.class))).thenReturn(userDto);
    when(userAuthenticationProvider.createToken(any(UserDto.class))).thenReturn("token");

    // When
    ResponseEntity<UserDto> response = authController.login(credentialsDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("email@example.com", Objects.requireNonNull(response.getBody()).getEmail());
    assertEquals("token", response.getBody().getToken());
  }

  @Test
  void loginWithInvalidCredentials() {
    // Given
    char[] password = {'t', 'e', 's', 't'};
    CredentialsDto credentialsDto = new CredentialsDto("username", password);

    when(userService.login(any(CredentialsDto.class)))
        .thenThrow(new AppException("Invalid credentials", HttpStatus.UNAUTHORIZED));

    // When & Then
    AppException exception = assertThrows(AppException.class,
        () -> authController.login(credentialsDto));

    assertEquals("Invalid credentials", exception.getMessage());
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
  }

  @Test
  void registerWithValidData() {
    // Given
    SignUpDto signUpDto = new SignUpDto("name", "surname", "email@example.com", "password");
    UserDto userDto = new UserDto();
    userDto.setId(1L);
    userDto.setEmail("email@example.com");

    when(userService.register(any(SignUpDto.class))).thenReturn(userDto);

    // When
    ResponseEntity<UserDto> response = authController.register(signUpDto);

    // Then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("email@example.com", response.getBody().getEmail());
  }

  @Test
  void registerWithInvalidEmail() {
    // Given
    SignUpDto signUpDto = new SignUpDto("name", "surname", "invalid-email", "password");

    when(userService.register(any(SignUpDto.class)))
        .thenThrow(new AppException("Invalid email format", HttpStatus.BAD_REQUEST));

    // When & Then
    AppException exception = assertThrows(AppException.class,
        () -> authController.register(signUpDto));

    assertEquals("Invalid email format", exception.getMessage());
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
  }
}
