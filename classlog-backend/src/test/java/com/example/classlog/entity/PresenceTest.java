package com.example.classlog.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PresenceTest {

  private User student;
  private Lesson lesson;

  @BeforeEach
  void setUp() {
    // Setting up test data for User and Lesson entities
    student = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    lesson = Lesson.builder()
        .lessonId(1L)
        .subject("Math 101")
        .lessonDate(LocalDateTime.now())
        .build();
  }

  @Test
  void shouldCreatePresenceUsingConstructor() {
    // Given
    Long presenceId = 1L;

    // When
    Presence presence = new Presence(presenceId, student, lesson);

    // Then
    assertThat(presence.getPresenceId()).isEqualTo(presenceId);
    assertThat(presence.getStudent()).isEqualTo(student);
    assertThat(presence.getLesson()).isEqualTo(lesson);
  }

  @Test
  void shouldCreatePresenceUsingBuilder() {
    // Given
    Long presenceId = 1L;

    // When
    Presence presence = Presence.builder()
        .presenceId(presenceId)
        .student(student)
        .lesson(lesson)
        .build();

    // Then
    assertThat(presence.getPresenceId()).isEqualTo(presenceId);
    assertThat(presence.getStudent()).isEqualTo(student);
    assertThat(presence.getLesson()).isEqualTo(lesson);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    Presence presence = Presence.builder().build();

    // When
    presence.setPresenceId(1L);
    presence.setStudent(student);
    presence.setLesson(lesson);

    // Then
    assertThat(presence.getPresenceId()).isEqualTo(1L);
    assertThat(presence.getStudent()).isEqualTo(student);
    assertThat(presence.getLesson()).isEqualTo(lesson);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    Presence presence1 = Presence.builder()
        .presenceId(1L)
        .student(student)
        .lesson(lesson)
        .build();

    Presence presence2 = Presence.builder()
        .presenceId(1L)
        .student(student)
        .lesson(lesson)
        .build();

    // When & Then
    assertThat(presence1).isEqualTo(presence2);
    assertThat(presence1.hashCode()).isEqualTo(presence2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    Presence presence = Presence.builder()
        .presenceId(1L)
        .student(student)
        .lesson(lesson)
        .build();

    // When & Then
    String toString = presence.toString();
    // Check for relevant fields in toString
    assertThat(toString).contains("presenceId=1");
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    Presence presence = new Presence();

    // When & Then
    assertThat(presence.getPresenceId()).isNull();
    assertThat(presence.getStudent()).isNull();
    assertThat(presence.getLesson()).isNull();
  }

  @Test
  void shouldHandleNullValuesForBuilder() {
    // Given
    Presence presence = Presence.builder().build();

    // When & Then
    assertThat(presence.getPresenceId()).isNull();
    assertThat(presence.getStudent()).isNull();
    assertThat(presence.getLesson()).isNull();
  }
}
