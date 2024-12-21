package com.example.classlog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.example.classlog.dto.ChangePasswordDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.Role;
import com.example.classlog.service.UserService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private UserDto userDto;
  private Role role;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    role = new Role();
    role.setId(1L);
    role.setRoleName("Admin");

    userDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .role(role)
        .createdAt(LocalDateTime.now())
        .token("sample-token")
        .build();
  }

  @Test
  void getAllUsers() {
    // Given
    List<UserDto> users = Arrays.asList(userDto, userDto);
    Mockito.when(userService.findAllUsers()).thenReturn(users);

    // When
    ResponseEntity<List<UserDto>> response = userController.getAllUsers();

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
  }

  @Test
  void getUsersByRole() {
    // Given
    List<UserDto> usersWithRole = Arrays.asList(userDto);
    Mockito.when(userService.findByRole(1L)).thenReturn(usersWithRole);

    // When
    ResponseEntity<List<UserDto>> response = userController.getUsersByRole(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void getUser() {
    // Given
    Mockito.when(userService.findUserById(1L)).thenReturn(userDto);

    // When
    ResponseEntity<UserDto> response = userController.getUser(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userDto.getId(), response.getBody().getId());
  }

  @Test
  void deleteUser() {
    // When
    ResponseEntity<Void> response = userController.deleteUser(1L);

    // Then
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  void updateUser() {
    // Given
    Mockito.when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(userDto);

    // When
    ResponseEntity<UserDto> response = userController.updateUser(1L, userDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userDto.getId(), response.getBody().getId());
  }

  @Test
  void changePassword() {
    // Given
    ChangePasswordDto changePasswordDto = new ChangePasswordDto();
    Mockito.when(userService.changePassword(any(ChangePasswordDto.class))).thenReturn(userDto);

    // When
    ResponseEntity<UserDto> response = userController.changePassword(changePasswordDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userDto.getId(), response.getBody().getId());
  }

  @Test
  void getUsersByClass() {
    // Given
    List<UserDto> users = Arrays.asList(userDto, userDto);
    Mockito.when(userService.getUsersByClass(1L)).thenReturn(users);

    // When
    ResponseEntity<List<UserDto>> response = userController.getUsersByClass(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
  }

  @Test
  void getUsersNotFromClass() {
    // Given
    List<UserDto> users = Arrays.asList(userDto);
    Mockito.when(userService.getUsersNotFromClass(1L)).thenReturn(users);

    // When
    ResponseEntity<List<UserDto>> response = userController.getUsersNotFromClass(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void getUsersByClassAndRole() {
    // Given
    List<UserDto> users = Arrays.asList(userDto);
    Mockito.when(userService.getUsersByClass(1L)).thenReturn(users);

    // When
    ResponseEntity<List<UserDto>> response = userController.getUsersByClassAndRole(1L, 1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
  }
}
