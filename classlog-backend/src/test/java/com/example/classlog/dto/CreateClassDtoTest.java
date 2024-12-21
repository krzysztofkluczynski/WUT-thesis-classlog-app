package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class CreateClassDtoTest {

  @Test
  void shouldCreateCreateClassDtoUsingBuilder() {
    // Given
    UserDto createdBy = UserDto.builder().id(1L).name("John").email("john@example.com").build();
    ClassDto classDto = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();

    // When
    CreateClassDto createClassDto = CreateClassDto.builder()
        .createdBy(createdBy)
        .classDto(classDto)
        .build();

    // Then
    assertThat(createClassDto.getCreatedBy()).isEqualTo(createdBy);
    assertThat(createClassDto.getClassDto()).isEqualTo(classDto);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    CreateClassDto createClassDto = CreateClassDto.builder().build();

    // When
    UserDto createdBy = UserDto.builder().id(1L).name("John").email("john@example.com").build();
    ClassDto classDto = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();

    createClassDto.setCreatedBy(createdBy);
    createClassDto.setClassDto(classDto);

    // Then
    assertThat(createClassDto.getCreatedBy()).isEqualTo(createdBy);
    assertThat(createClassDto.getClassDto()).isEqualTo(classDto);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    UserDto createdBy1 = UserDto.builder().id(1L).name("John").email("john@example.com").build();
    ClassDto classDto1 = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();

    CreateClassDto createClassDto1 = CreateClassDto.builder()
        .createdBy(createdBy1)
        .classDto(classDto1)
        .build();

    UserDto createdBy2 = UserDto.builder().id(1L).name("John").email("john@example.com").build();
    ClassDto classDto2 = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(classDto1.getCreatedAt())  // Ensure same timestamp for comparison
        .build();

    CreateClassDto createClassDto2 = CreateClassDto.builder()
        .createdBy(createdBy2)
        .classDto(classDto2)
        .build();

    // When & Then
    assertThat(createClassDto1)
        .isEqualToIgnoringGivenFields(createClassDto2, "createdBy.createdAt", "classDto.createdAt");

    // Optionally, check hashCodes to ensure they match as well
    assertThat(createClassDto1.hashCode()).isEqualTo(createClassDto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    UserDto createdBy = UserDto.builder().id(1L).name("John").email("john@example.com").build();
    ClassDto classDto = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();

    CreateClassDto createClassDto = CreateClassDto.builder()
        .createdBy(createdBy)
        .classDto(classDto)
        .build();

    // When & Then
    String toString = createClassDto.toString();

    // Adjusting toString to expect all fields including those set to null (like `role`, `createdAt`, etc.)
    assertThat(toString).contains(
        "createdBy=UserDto(id=1, name=John, surname=null, email=john@example.com, role=null, createdAt=null, token=null)",
        "classDto=ClassDto(id=1, name=Math 101, description=Introduction to Mathematics, code=MATH101, createdAt=");
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    CreateClassDto createClassDto = CreateClassDto.builder().build();

    // When & Then
    assertThat(createClassDto.getCreatedBy()).isNull();
    assertThat(createClassDto.getClassDto()).isNull();
  }
}
