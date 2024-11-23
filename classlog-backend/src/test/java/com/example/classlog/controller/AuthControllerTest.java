package com.example.classlog.controller;

import com.example.classlog.dto.CredentialsDto;
import com.example.classlog.dto.SignUpDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.Role;
import com.example.classlog.entities.User;
import com.example.classlog.exception.AppException;
import com.example.classlog.repository.UserRepository;
import com.example.classlog.service.UserService;
import com.example.classlog.config.UserAuthenticationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

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
        char[] password = {'t', 'e', 's', 't'};
        CredentialsDto credentialsDto = new CredentialsDto("username", password);
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("email");
        userDto.setToken("token");

        when(userService.login(any(CredentialsDto.class))).thenReturn(userDto);
        when(userAuthenticationProvider.createToken(any(UserDto.class))).thenReturn("token");

        ResponseEntity<UserDto> response = authController.login(credentialsDto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("email", response.getBody().getEmail());
        assertEquals("token", response.getBody().getToken());
    }

    @Test
    void loginInvalidCredentials() {
        // Given
        char[] password = {'t', 'e', 's', 't'};
        SignUpDto signUpDto = new SignUpDto("name", "surname",  "email@example.com", "password");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("email@example.com");

        // When
        when(userService.register(any(SignUpDto.class))).thenReturn(userDto);

        //then
        ResponseEntity<UserDto> response = authController.register(signUpDto);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("/users/1", response.getHeaders().getLocation().toString());
        assertEquals("email@example.com", response.getBody().getEmail());

    }

}
