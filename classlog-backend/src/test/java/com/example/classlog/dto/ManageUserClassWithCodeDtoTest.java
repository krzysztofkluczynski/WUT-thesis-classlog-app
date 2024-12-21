package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ManageUserClassWithCodeDtoTest {

  @Test
  void shouldCreateManageUserClassWithCodeDtoUsingBuilder() {
    // Given
    String classCode = "MATH101";
    UserDto user = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    // When
    ManageUserClassWithCodeDto manageUserClassWithCodeDto = ManageUserClassWithCodeDto.builder()
        .classCode(classCode)
        .user(user)
        .build();

    // Then
    assertThat(manageUserClassWithCodeDto.getClassCode()).isEqualTo(classCode);
    assertThat(manageUserClassWithCodeDto.getUser()).isEqualTo(user);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    ManageUserClassWithCodeDto manageUserClassWithCodeDto = ManageUserClassWithCodeDto.builder()
        .build();

    // When
    String classCode = "MATH101";
    UserDto user = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    manageUserClassWithCodeDto.setClassCode(classCode);
    manageUserClassWithCodeDto.setUser(user);

    // Then
    assertThat(manageUserClassWithCodeDto.getClassCode()).isEqualTo(classCode);
    assertThat(manageUserClassWithCodeDto.getUser()).isEqualTo(user);
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
    ManageUserClassWithCodeDto dto1 = ManageUserClassWithCodeDto.builder()
        .classCode("MATH101")
        .user(user1)
        .build();

    UserDto user2 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();
    ManageUserClassWithCodeDto dto2 = ManageUserClassWithCodeDto.builder()
        .classCode("MATH101")
        .user(user2)
        .build();

    // When & Then
    assertThat(dto1).isEqualTo(dto2);
    assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    String classCode = "MATH101";
    UserDto user = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();
    ManageUserClassWithCodeDto manageUserClassWithCodeDto = ManageUserClassWithCodeDto.builder()
        .classCode(classCode)
        .user(user)
        .build();

    // When & Then
    String toString = manageUserClassWithCodeDto.toString();

    // Simplified toString, omitting the UserDto details
    assertThat(toString).contains(
        "classCode=MATH101"
    );
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    ManageUserClassWithCodeDto manageUserClassWithCodeDto = ManageUserClassWithCodeDto.builder()
        .build();

    // When & Then
    assertThat(manageUserClassWithCodeDto.getClassCode()).isNull();
    assertThat(manageUserClassWithCodeDto.getUser()).isNull();
  }
}
