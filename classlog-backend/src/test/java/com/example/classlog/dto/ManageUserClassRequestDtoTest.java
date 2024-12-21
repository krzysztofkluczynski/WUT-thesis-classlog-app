package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ManageUserClassRequestDtoTest {

  @Test
  void shouldCreateManageUserClassRequestDtoUsingBuilder() {
    // Given
    Long classId = 1L;
    UserDto user1 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();
    UserDto user2 = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .build();
    List<UserDto> users = Arrays.asList(user1, user2);

    // When
    ManageUserClassRequestDto manageUserClassRequestDto = ManageUserClassRequestDto.builder()
        .classId(classId)
        .users(users)
        .build();

    // Then
    assertThat(manageUserClassRequestDto.getClassId()).isEqualTo(classId);
    assertThat(manageUserClassRequestDto.getUsers()).isEqualTo(users);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    ManageUserClassRequestDto manageUserClassRequestDto = ManageUserClassRequestDto.builder()
        .build();

    // When
    Long classId = 1L;
    UserDto user1 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();
    UserDto user2 = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .build();
    List<UserDto> users = Arrays.asList(user1, user2);

    manageUserClassRequestDto.setClassId(classId);
    manageUserClassRequestDto.setUsers(users);

    // Then
    assertThat(manageUserClassRequestDto.getClassId()).isEqualTo(classId);
    assertThat(manageUserClassRequestDto.getUsers()).isEqualTo(users);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    UserDto user1 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();
    UserDto user2 = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .build();
    List<UserDto> users1 = Arrays.asList(user1, user2);
    ManageUserClassRequestDto request1 = ManageUserClassRequestDto.builder()
        .classId(1L)
        .users(users1)
        .build();

    List<UserDto> users2 = Arrays.asList(user1, user2);
    ManageUserClassRequestDto request2 = ManageUserClassRequestDto.builder()
        .classId(1L)
        .users(users2)
        .build();

    // When & Then
    assertThat(request1).isEqualTo(request2);
    assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    UserDto user1 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();
    UserDto user2 = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .build();
    List<UserDto> users = Arrays.asList(user1, user2);

    ManageUserClassRequestDto manageUserClassRequestDto = ManageUserClassRequestDto.builder()
        .classId(1L)
        .users(users)
        .build();

    // When & Then
    String toString = manageUserClassRequestDto.toString();

    // Check for key fields, but omit the external classes (UserDto) details
    assertThat(toString).contains(
        "classId=1"
    );
  }
  
}
