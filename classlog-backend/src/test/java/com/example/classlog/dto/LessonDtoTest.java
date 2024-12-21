package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class LessonDtoTest {

  @Test
  void shouldCreateLessonDtoUsingBuilder() {
    // Given
    Long lessonId = 1L;
    UserDto createdByUser = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    ClassDto lessonClass = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();
    LocalDateTime lessonDate = LocalDateTime.now();
    String subject = "Mathematics";
    String content = "Introduction to algebra";

    // When
    LessonDto lessonDto = LessonDto.builder()
        .lessonId(lessonId)
        .createdByUser(createdByUser)
        .lessonClass(lessonClass)
        .lessonDate(lessonDate)
        .subject(subject)
        .content(content)
        .build();

    // Then
    assertThat(lessonDto.getLessonId()).isEqualTo(lessonId);
    assertThat(lessonDto.getCreatedByUser()).isEqualTo(createdByUser);
    assertThat(lessonDto.getLessonClass()).isEqualTo(lessonClass);
    assertThat(lessonDto.getLessonDate()).isEqualTo(lessonDate);
    assertThat(lessonDto.getSubject()).isEqualTo(subject);
    assertThat(lessonDto.getContent()).isEqualTo(content);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    LessonDto lessonDto = LessonDto.builder().build();

    // When
    UserDto createdByUser = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    ClassDto lessonClass = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();
    LocalDateTime lessonDate = LocalDateTime.now();
    String subject = "Mathematics";
    String content = "Introduction to algebra";

    lessonDto.setLessonId(1L);
    lessonDto.setCreatedByUser(createdByUser);
    lessonDto.setLessonClass(lessonClass);
    lessonDto.setLessonDate(lessonDate);
    lessonDto.setSubject(subject);
    lessonDto.setContent(content);

    // Then
    assertThat(lessonDto.getLessonId()).isEqualTo(1L);
    assertThat(lessonDto.getCreatedByUser()).isEqualTo(createdByUser);
    assertThat(lessonDto.getLessonClass()).isEqualTo(lessonClass);
    assertThat(lessonDto.getLessonDate()).isEqualTo(lessonDate);
    assertThat(lessonDto.getSubject()).isEqualTo(subject);
    assertThat(lessonDto.getContent()).isEqualTo(content);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    UserDto createdByUser1 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    ClassDto lessonClass1 = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();
    LocalDateTime lessonDate1 = LocalDateTime.now();
    String subject1 = "Mathematics";
    String content1 = "Introduction to algebra";

    LessonDto lessonDto1 = LessonDto.builder()
        .lessonId(1L)
        .createdByUser(createdByUser1)
        .lessonClass(lessonClass1)
        .lessonDate(lessonDate1)
        .subject(subject1)
        .content(content1)
        .build();

    UserDto createdByUser2 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(createdByUser1.getCreatedAt())
        .build();
    ClassDto lessonClass2 = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(lessonClass1.getCreatedAt())
        .build();
    LocalDateTime lessonDate2 = lessonDate1; // Same timestamp for comparison
    String subject2 = "Mathematics";
    String content2 = "Introduction to algebra";

    LessonDto lessonDto2 = LessonDto.builder()
        .lessonId(1L)
        .createdByUser(createdByUser2)
        .lessonClass(lessonClass2)
        .lessonDate(lessonDate2)
        .subject(subject2)
        .content(content2)
        .build();

    // When & Then
    assertThat(lessonDto1)
        .isEqualToIgnoringGivenFields(lessonDto2, "lessonDate", "createdByUser.createdAt",
            "lessonClass.createdAt");
    assertThat(lessonDto1.hashCode()).isEqualTo(lessonDto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    UserDto createdByUser = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    ClassDto lessonClass = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();
    LocalDateTime lessonDate = LocalDateTime.now();
    String subject = "Mathematics";
    String content = "Introduction to algebra";

    LessonDto lessonDto = LessonDto.builder()
        .lessonId(1L)
        .createdByUser(createdByUser)
        .lessonClass(lessonClass)
        .lessonDate(lessonDate)
        .subject(subject)
        .content(content)
        .build();

    // When & Then
    String toString = lessonDto.toString();

    // Check for key fields, ignoring the `lessonDate` field for comparison
    assertThat(toString).contains(
        "lessonId=1",
        "subject=Mathematics",
        "content=Introduction to algebra"
    );
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    LessonDto lessonDto = LessonDto.builder().build();

    // When & Then
    assertThat(lessonDto.getLessonId()).isNull();
    assertThat(lessonDto.getCreatedByUser()).isNull();
    assertThat(lessonDto.getLessonClass()).isNull();
    assertThat(lessonDto.getLessonDate()).isNull();
    assertThat(lessonDto.getSubject()).isNull();
    assertThat(lessonDto.getContent()).isNull();
  }
}
