package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class GradeDtoTest {

  @Test
  void shouldCreateGradeDtoUsingBuilder() {
    // Given
    Long gradeId = 1L;
    ClassDto assignedClass = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto student = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto teacher = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    Integer grade = 85;
    Integer wage = 100;
    String description = "Final grade for the course";
    LocalDateTime createdAt = LocalDateTime.now();

    // When
    GradeDto gradeDto = GradeDto.builder()
        .gradeId(gradeId)
        .assignedClass(assignedClass)
        .student(student)
        .teacher(teacher)
        .grade(grade)
        .wage(wage)
        .description(description)
        .createdAt(createdAt)
        .build();

    // Then
    assertThat(gradeDto.getGradeId()).isEqualTo(gradeId);
    assertThat(gradeDto.getAssignedClass()).isEqualTo(assignedClass);
    assertThat(gradeDto.getStudent()).isEqualTo(student);
    assertThat(gradeDto.getTeacher()).isEqualTo(teacher);
    assertThat(gradeDto.getGrade()).isEqualTo(grade);
    assertThat(gradeDto.getWage()).isEqualTo(wage);
    assertThat(gradeDto.getDescription()).isEqualTo(description);
    assertThat(gradeDto.getCreatedAt()).isEqualTo(createdAt);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    GradeDto gradeDto = GradeDto.builder().build();

    // When
    ClassDto assignedClass = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto student = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto teacher = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    Integer grade = 85;
    Integer wage = 100;
    String description = "Final grade for the course";
    LocalDateTime createdAt = LocalDateTime.now();

    gradeDto.setGradeId(1L);
    gradeDto.setAssignedClass(assignedClass);
    gradeDto.setStudent(student);
    gradeDto.setTeacher(teacher);
    gradeDto.setGrade(grade);
    gradeDto.setWage(wage);
    gradeDto.setDescription(description);
    gradeDto.setCreatedAt(createdAt);

    // Then
    assertThat(gradeDto.getGradeId()).isEqualTo(1L);
    assertThat(gradeDto.getAssignedClass()).isEqualTo(assignedClass);
    assertThat(gradeDto.getStudent()).isEqualTo(student);
    assertThat(gradeDto.getTeacher()).isEqualTo(teacher);
    assertThat(gradeDto.getGrade()).isEqualTo(grade);
    assertThat(gradeDto.getWage()).isEqualTo(wage);
    assertThat(gradeDto.getDescription()).isEqualTo(description);
    assertThat(gradeDto.getCreatedAt()).isEqualTo(createdAt);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    ClassDto assignedClass1 = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto student1 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto teacher1 = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    GradeDto gradeDto1 = GradeDto.builder()
        .gradeId(1L)
        .assignedClass(assignedClass1)
        .student(student1)
        .teacher(teacher1)
        .grade(85)
        .wage(100)
        .description("Final grade for the course")
        .createdAt(LocalDateTime.now())
        .build();

    ClassDto assignedClass2 = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(assignedClass1.getCreatedAt())
        .build();
    UserDto student2 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(student1.getCreatedAt())
        .build();
    UserDto teacher2 = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .createdAt(teacher1.getCreatedAt())
        .build();
    GradeDto gradeDto2 = GradeDto.builder()
        .gradeId(1L)
        .assignedClass(assignedClass2)
        .student(student2)
        .teacher(teacher2)
        .grade(85)
        .wage(100)
        .description("Final grade for the course")
        .createdAt(gradeDto1.getCreatedAt())
        .build();

    // When & Then
    assertThat(gradeDto1)
        .isEqualToIgnoringGivenFields(gradeDto2, "createdAt", "assignedClass.createdAt",
            "student.createdAt", "teacher.createdAt");
    assertThat(gradeDto1.hashCode()).isEqualTo(gradeDto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    ClassDto assignedClass = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto student = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto teacher = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    GradeDto gradeDto = GradeDto.builder()
        .gradeId(1L)
        .assignedClass(assignedClass)
        .student(student)
        .teacher(teacher)
        .grade(85)
        .wage(100)
        .description("Final grade for the course")
        .createdAt(LocalDateTime.now())
        .build();

    // When & Then
    String toString = gradeDto.toString();

    // We now check for the core fields, ignoring any createdAt fields (for both GradeDto and associated objects)
    assertThat(toString).contains(
        "gradeId=1",
        "grade=85",
        "wage=100"
    );
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    GradeDto gradeDto = GradeDto.builder().build();

    // When & Then
    assertThat(gradeDto.getGradeId()).isNull();
    assertThat(gradeDto.getAssignedClass()).isNull();
    assertThat(gradeDto.getStudent()).isNull();
    assertThat(gradeDto.getTeacher()).isNull();
    assertThat(gradeDto.getGrade()).isNull();
    assertThat(gradeDto.getWage()).isNull();
    assertThat(gradeDto.getDescription()).isNull();
    assertThat(gradeDto.getCreatedAt()).isNull();
  }
}
