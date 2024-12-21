package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ClassDtoTest {

  @Test
  void shouldCreateClassDtoUsingBuilder() {
    // Given
    Long id = 1L;
    String name = "Math 101";
    String description = "Introduction to Mathematics";
    String code = "MATH101";
    LocalDateTime createdAt = LocalDateTime.now();

    // When
    ClassDto classDto = ClassDto.builder()
        .id(id)
        .name(name)
        .description(description)
        .code(code)
        .createdAt(createdAt)
        .build();

    // Then
    assertThat(classDto.getId()).isEqualTo(id);
    assertThat(classDto.getName()).isEqualTo(name);
    assertThat(classDto.getDescription()).isEqualTo(description);
    assertThat(classDto.getCode()).isEqualTo(code);
    assertThat(classDto.getCreatedAt()).isEqualTo(createdAt);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    ClassDto classDto = ClassDto.builder().build();

    // When
    classDto.setId(1L);
    classDto.setName("Math 101");
    classDto.setDescription("Introduction to Mathematics");
    classDto.setCode("MATH101");
    classDto.setCreatedAt(LocalDateTime.now());

    // Then
    assertThat(classDto.getId()).isEqualTo(1L);
    assertThat(classDto.getName()).isEqualTo("Math 101");
    assertThat(classDto.getDescription()).isEqualTo("Introduction to Mathematics");
    assertThat(classDto.getCode()).isEqualTo("MATH101");
    assertThat(classDto.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    ClassDto classDto1 = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.MIN)
        .build();

    ClassDto classDto2 = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.MIN)
        .build();

    // When & Then
    assertThat(classDto1).isEqualTo(classDto2);
    assertThat(classDto1.hashCode()).isEqualTo(classDto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    ClassDto classDto = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();

    // When & Then
    assertThat(classDto.toString()).contains("id=1", "name=Math 101", "code=MATH101", "createdAt=");
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    ClassDto classDto = ClassDto.builder().build();

    // When & Then
    assertThat(classDto.getId()).isNull();
    assertThat(classDto.getName()).isNull();
    assertThat(classDto.getDescription()).isNull();
    assertThat(classDto.getCode()).isNull();
    assertThat(classDto.getCreatedAt()).isNull();
  }
}
