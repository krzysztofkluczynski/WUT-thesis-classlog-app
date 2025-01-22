package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class LessonPresenceDtoTest {

  @Test
  void shouldCreateLessonPresenceRequestUsingBuilder() {
    // Given
    long lessonId = 1L;
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
    LessonPresenceDto lessonPresenceDto = LessonPresenceDto.builder()
        .lessonId(lessonId)
        .users(users)
        .build();

    // Then
    assertThat(lessonPresenceDto.getLessonId()).isEqualTo(lessonId);
    assertThat(lessonPresenceDto.getUsers()).isEqualTo(users);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    LessonPresenceDto lessonPresenceDto = LessonPresenceDto.builder().build();

    // When
    long lessonId = 1L;
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

    lessonPresenceDto.setLessonId(lessonId);
    lessonPresenceDto.setUsers(users);

    // Then
    assertThat(lessonPresenceDto.getLessonId()).isEqualTo(lessonId);
    assertThat(lessonPresenceDto.getUsers()).isEqualTo(users);
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
    LessonPresenceDto request1 = LessonPresenceDto.builder()
        .lessonId(1L)
        .users(users1)
        .build();

    List<UserDto> users2 = Arrays.asList(user1, user2);
    LessonPresenceDto request2 = LessonPresenceDto.builder()
        .lessonId(1L)
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

    LessonPresenceDto lessonPresenceDto = LessonPresenceDto.builder()
        .lessonId(1L)
        .users(users)
        .build();

    // When & Then
    String toString = lessonPresenceDto.toString();
    // Simplified toString, focusing on key fields
    assertThat(toString).contains("lessonId=1");
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    LessonPresenceDto lessonPresenceDto = LessonPresenceDto.builder().build();

    // When & Then
    assertThat(lessonPresenceDto.getLessonId()).isEqualTo(0);  // default value for long
    assertThat(lessonPresenceDto.getUsers()).isNull();
  }
}
