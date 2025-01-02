package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.dto.SignUpDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entity.Role;
import com.example.classlog.entity.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserMapperTest {

  private UserMapper userMapper;

  private Role role;
  private User user;
  private UserDto userDto;
  private SignUpDto signUpDto;

  @BeforeEach
  void setUp() {
    userMapper = Mappers.getMapper(UserMapper.class);

    role = Role.builder()
        .id(1L)
        .roleName("Student")
        .build();

    user = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .password("securepassword")
        .createdAt(LocalDateTime.now())
        .role(role)
        .build();

    userDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .role(role)
        .createdAt(LocalDateTime.now())
        .token("sampletoken")
        .build();

    signUpDto = new SignUpDto("Jane", "Doe", "jane.doe@example.com", "securepassword");
  }

  @Test
  void shouldMapUserToUserDto() {
    // When
    UserDto result = userMapper.toUserDto(user);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(user.getId());
    assertThat(result.getName()).isEqualTo(user.getName());
    assertThat(result.getSurname()).isEqualTo(user.getSurname());
    assertThat(result.getEmail()).isEqualTo(user.getEmail());
    assertThat(result.getRole()).isEqualTo(user.getRole());
    assertThat(result.getCreatedAt()).isEqualTo(user.getCreatedAt());
  }

  @Test
  void shouldMapUserDtoToUser() {
    // When
    User result = userMapper.toUser(userDto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(userDto.getId());
    assertThat(result.getName()).isEqualTo(userDto.getName());
    assertThat(result.getSurname()).isEqualTo(userDto.getSurname());
    assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
    assertThat(result.getRole()).isEqualTo(userDto.getRole());
    assertThat(result.getCreatedAt()).isEqualTo(userDto.getCreatedAt());
  }

  @Test
  void shouldMapSignUpDtoToUser() {
    // When
    User result = userMapper.signUpToUser(signUpDto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo(signUpDto.name());
    assertThat(result.getSurname()).isEqualTo(signUpDto.surname());
    assertThat(result.getEmail()).isEqualTo(signUpDto.email());
    assertThat(result.getPassword()).isNull(); // Password should not be mapped
  }

  @Test
  void testUserEntityGettersAndSetters() {
    // Given
    User newUser = new User();
    newUser.setId(2L);
    newUser.setName("Jane");
    newUser.setSurname("Smith");
    newUser.setEmail("jane.smith@example.com");
    newUser.setPassword("newpassword");
    newUser.setCreatedAt(LocalDateTime.now());
    newUser.setRole(role);

    // Then
    assertThat(newUser.getId()).isEqualTo(2L);
    assertThat(newUser.getName()).isEqualTo("Jane");
    assertThat(newUser.getSurname()).isEqualTo("Smith");
    assertThat(newUser.getEmail()).isEqualTo("jane.smith@example.com");
    assertThat(newUser.getPassword()).isEqualTo("newpassword");
    assertThat(newUser.getRole()).isEqualTo(role);
  }

  @Test
  void testUserDtoGettersAndSetters() {
    // Given
    UserDto newUserDto = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .role(role)
        .createdAt(LocalDateTime.now())
        .token("newtoken")
        .build();

    // Then
    assertThat(newUserDto.getId()).isEqualTo(2L);
    assertThat(newUserDto.getName()).isEqualTo("Jane");
    assertThat(newUserDto.getSurname()).isEqualTo("Smith");
    assertThat(newUserDto.getEmail()).isEqualTo("jane.smith@example.com");
    assertThat(newUserDto.getRole()).isEqualTo(role);
    assertThat(newUserDto.getToken()).isEqualTo("newtoken");
  }

  @Test
  void testSignUpDto() {
    // Given
    SignUpDto newSignUpDto = new SignUpDto("Mark", "Johnson", "mark.johnson@example.com",
        "secure123");

    // Then
    assertThat(newSignUpDto.name()).isEqualTo("Mark");
    assertThat(newSignUpDto.surname()).isEqualTo("Johnson");
    assertThat(newSignUpDto.email()).isEqualTo("mark.johnson@example.com");
    assertThat(newSignUpDto.password()).isEqualTo("secure123");
  }
}
