package com.example.classlog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.dto.ChangePasswordDto;
import com.example.classlog.dto.CredentialsDto;
import com.example.classlog.dto.SignUpDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entity.Role;
import com.example.classlog.entity.User;
import com.example.classlog.mapper.UserMapper;
import com.example.classlog.repository.RoleRepository;
import com.example.classlog.repository.UserRepository;
import java.nio.CharBuffer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private UserService userService;

  private User user;
  private UserDto userDto;
  private Role role;
  private SignUpDto signUpDto;
  private CredentialsDto credentialsDto;
  private ChangePasswordDto changePasswordDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    role = Role.builder()
        .id(4L)
        .roleName("Student")
        .build();

    user = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .password("encodedPassword")
        .role(role)
        .createdAt(LocalDateTime.now())
        .build();

    userDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .role(role)
        .createdAt(LocalDateTime.now())
        .token("dummyToken")
        .build();

    signUpDto = new SignUpDto("John", "Doe", "john.doe@example.com", "password123");

    credentialsDto = new CredentialsDto("john.doe@example.com", "password123".toCharArray());

    changePasswordDto = new ChangePasswordDto();
    changePasswordDto.setUserId(1L);
    changePasswordDto.setOldPassword("oldPassword");
    changePasswordDto.setNewPassword("newPassword");
  }

  @Test
  void shouldLoginSuccessfully() {
    // Given
    when(userRepository.findByEmail(credentialsDto.email())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),
        user.getPassword())).thenReturn(true);
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    UserDto result = userService.login(credentialsDto);

    // Then
    assertThat(result).isEqualTo(userDto);
    verify(userRepository, times(1)).findByEmail(credentialsDto.email());
    verify(passwordEncoder, times(1)).matches(CharBuffer.wrap(credentialsDto.password()),
        user.getPassword());
  }

  @Test
  void shouldThrowExceptionForInvalidLogin() {
    // Given
    when(userRepository.findByEmail(credentialsDto.email())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),
        user.getPassword())).thenReturn(false);

    // When & Then
    assertThatThrownBy(() -> userService.login(credentialsDto))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("Invalid password");
  }

  @Test
  void shouldRegisterSuccessfully() {
    // Given
    when(userRepository.findByEmail(signUpDto.email())).thenReturn(Optional.empty());
    when(roleRepository.findById(4L)).thenReturn(Optional.of(role));
    when(userMapper.signUpToUser(signUpDto)).thenReturn(user);
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    UserDto result = userService.register(signUpDto);

    // Then
    assertThat(result).isEqualTo(userDto);
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void shouldThrowExceptionWhenRegisteringWithExistingEmail() {
    // Given
    when(userRepository.findByEmail(signUpDto.email())).thenReturn(Optional.of(user));

    // When & Then
    assertThatThrownBy(() -> userService.register(signUpDto))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("Email already exists");
  }

  @Test
  void shouldFindUserByEmail() {
    // Given
    when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    UserDto result = userService.findByEmail("john.doe@example.com");

    // Then
    assertThat(result).isEqualTo(userDto);
    verify(userRepository, times(1)).findByEmail("john.doe@example.com");
  }

  @Test
  void shouldChangePasswordSuccessfully() {
    // Given
    when(userRepository.findById(changePasswordDto.getUserId())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(CharBuffer.wrap(changePasswordDto.getOldPassword()),
        user.getPassword())).thenReturn(true);
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    UserDto result = userService.changePassword(changePasswordDto);

    // Then
    assertThat(result).isEqualTo(userDto);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void shouldThrowExceptionForInvalidOldPassword() {
    // Given
    when(userRepository.findById(changePasswordDto.getUserId())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(CharBuffer.wrap(changePasswordDto.getOldPassword()),
        user.getPassword())).thenReturn(false);

    // When & Then
    assertThatThrownBy(() -> userService.changePassword(changePasswordDto))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("Invalid old password");
  }

  @Test
  void shouldDeleteUserSuccessfully() {
    // Given
    when(userRepository.existsById(1L)).thenReturn(true);

    // When
    userService.deleteUser(1L);

    // Then
    verify(userRepository, times(1)).deleteById(1L);
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistentUser() {
    // Given
    when(userRepository.existsById(1L)).thenReturn(false);

    // When & Then
    assertThatThrownBy(() -> userService.deleteUser(1L))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("User not found");
  }

  @Test
  void shouldUpdateUserSuccessfully() {
    // Given
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    UserDto updatedDto = UserDto.builder()
        .id(1L)
        .name("Updated")
        .surname("User")
        .email("updated.user@example.com")
        .role(role)
        .build();

    // When
    UserDto result = userService.updateUser(1L, updatedDto);

    // Then
    assertThat(result).isEqualTo(userDto);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void shouldFindAllUsers() {
    // Given
    when(userRepository.findAll()).thenReturn(List.of(user));
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    List<UserDto> result = userService.findAllUsers();

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getEmail()).isEqualTo("john.doe@example.com");
    verify(userRepository, times(1)).findAll();
  }

  @Test
  void shouldFindByRole() {
    // Given
    when(userRepository.findByRoleId(4L)).thenReturn(List.of(user));
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    List<UserDto> result = userService.findByRole(4L);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getRole().getRoleName()).isEqualTo("Student");
    verify(userRepository, times(1)).findByRoleId(4L);
  }

  @Test
  void shouldFindUserById() {
    // Given
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    UserDto result = userService.findUserById(1L);

    // Then
    assertThat(result).isEqualTo(userDto);
    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  void shouldThrowExceptionWhenUserNotFoundById() {
    // Given
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> userService.findUserById(1L))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("User not found");
  }

  @Test
  void shouldGetUsersByClass() {
    // Given
    when(userRepository.findByClassId(1L)).thenReturn(List.of(user));
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    List<UserDto> result = userService.getUsersByClass(1L);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getEmail()).isEqualTo("john.doe@example.com");
    verify(userRepository, times(1)).findByClassId(1L);
  }

  @Test
  void shouldGetUsersNotFromClass() {
    // Given
    when(userRepository.findUsersNotFromClass(1L)).thenReturn(List.of(user));
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    List<UserDto> result = userService.getUsersNotFromClass(1L);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getEmail()).isEqualTo("john.doe@example.com");
    verify(userRepository, times(1)).findUsersNotFromClass(1L);
  }

}
