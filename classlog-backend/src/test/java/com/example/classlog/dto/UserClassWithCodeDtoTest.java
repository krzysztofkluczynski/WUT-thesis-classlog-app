package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserClassWithCodeDtoTest {

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
    UserClassWithCodeDto userClassWithCodeDto = UserClassWithCodeDto.builder()
        .classCode(classCode)
        .user(user)
        .build();

    // Then
    assertThat(userClassWithCodeDto.getClassCode()).isEqualTo(classCode);
    assertThat(userClassWithCodeDto.getUser()).isEqualTo(user);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    UserClassWithCodeDto userClassWithCodeDto = UserClassWithCodeDto.builder()
        .build();

    // When
    String classCode = "MATH101";
    UserDto user = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    userClassWithCodeDto.setClassCode(classCode);
    userClassWithCodeDto.setUser(user);

    // Then
    assertThat(userClassWithCodeDto.getClassCode()).isEqualTo(classCode);
    assertThat(userClassWithCodeDto.getUser()).isEqualTo(user);
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
    UserClassWithCodeDto dto1 = UserClassWithCodeDto.builder()
        .classCode("MATH101")
        .user(user1)
        .build();

    UserDto user2 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();
    UserClassWithCodeDto dto2 = UserClassWithCodeDto.builder()
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
    UserClassWithCodeDto userClassWithCodeDto = UserClassWithCodeDto.builder()
        .classCode(classCode)
        .user(user)
        .build();

    // When & Then
    String toString = userClassWithCodeDto.toString();

    // Simplified toString, omitting the UserDto details
    assertThat(toString).contains(
        "classCode=MATH101"
    );
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    UserClassWithCodeDto userClassWithCodeDto = UserClassWithCodeDto.builder()
        .build();

    // When & Then
    assertThat(userClassWithCodeDto.getClassCode()).isNull();
    assertThat(userClassWithCodeDto.getUser()).isNull();
  }
}
