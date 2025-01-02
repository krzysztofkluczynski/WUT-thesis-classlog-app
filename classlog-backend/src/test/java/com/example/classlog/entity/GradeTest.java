package com.example.classlog.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GradeTest {

  private Class assignedClass;
  private User student;
  private User teacher;
  private Grade grade;

  @BeforeEach
  void setUp() {
    // Setting up test data for Class and User entities
    assignedClass = Class.builder().id(101L).name("Math 101").build();
    student = User.builder().id(1L).name("John").surname("Doe").email("john.doe@example.com")
        .build();
    teacher = User.builder().id(2L).name("Jane").surname("Smith").email("jane.smith@example.com")
        .build();

    grade = Grade.builder()
        .gradeId(1L)
        .assignedClass(assignedClass)
        .student(student)
        .teacher(teacher)
        .grade(85)
        .wage(100)
        .description("Final grade")
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Test
  void shouldCreateGradeUsingConstructor() {
    // Given
    Long gradeId = 1L;
    Integer gradeValue = 85;
    Integer wageValue = 100;
    String description = "Final grade";

    // When
    Grade grade = new Grade(gradeId, assignedClass, student, teacher, gradeValue, wageValue,
        description, LocalDateTime.now());

    // Then
    assertThat(grade.getGradeId()).isEqualTo(gradeId);
    assertThat(grade.getAssignedClass()).isEqualTo(assignedClass);
    assertThat(grade.getStudent()).isEqualTo(student);
    assertThat(grade.getTeacher()).isEqualTo(teacher);
    assertThat(grade.getGrade()).isEqualTo(gradeValue);
    assertThat(grade.getWage()).isEqualTo(wageValue);
    assertThat(grade.getDescription()).isEqualTo(description);
    assertThat(grade.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldCreateGradeUsingBuilder() {
    // Given
    Long gradeId = 1L;
    Integer gradeValue = 85;
    Integer wageValue = 100;
    String description = "Final grade";

    // When
    Grade grade = Grade.builder()
        .gradeId(gradeId)
        .assignedClass(assignedClass)
        .student(student)
        .teacher(teacher)
        .grade(gradeValue)
        .wage(wageValue)
        .description(description)
        .createdAt(LocalDateTime.now())
        .build();

    // Then
    assertThat(grade.getGradeId()).isEqualTo(gradeId);
    assertThat(grade.getAssignedClass()).isEqualTo(assignedClass);
    assertThat(grade.getStudent()).isEqualTo(student);
    assertThat(grade.getTeacher()).isEqualTo(teacher);
    assertThat(grade.getGrade()).isEqualTo(gradeValue);
    assertThat(grade.getWage()).isEqualTo(wageValue);
    assertThat(grade.getDescription()).isEqualTo(description);
    assertThat(grade.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    Grade grade = new Grade();

    // When
    grade.setGradeId(1L);
    grade.setAssignedClass(assignedClass);
    grade.setStudent(student);
    grade.setTeacher(teacher);
    grade.setGrade(85);
    grade.setWage(100);
    grade.setDescription("Final grade");
    grade.setCreatedAt(LocalDateTime.now());

    // Then
    assertThat(grade.getGradeId()).isEqualTo(1L);
    assertThat(grade.getAssignedClass()).isEqualTo(assignedClass);
    assertThat(grade.getStudent()).isEqualTo(student);
    assertThat(grade.getTeacher()).isEqualTo(teacher);
    assertThat(grade.getGrade()).isEqualTo(85);
    assertThat(grade.getWage()).isEqualTo(100);
    assertThat(grade.getDescription()).isEqualTo("Final grade");
    assertThat(grade.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    Grade grade1 = Grade.builder()
        .gradeId(1L)
        .assignedClass(assignedClass)
        .student(student)
        .teacher(teacher)
        .grade(85)
        .wage(100)
        .description("Final grade")
        .createdAt(LocalDateTime.now())
        .build();

    Grade grade2 = Grade.builder()
        .gradeId(1L)
        .assignedClass(assignedClass)
        .student(student)
        .teacher(teacher)
        .grade(85)
        .wage(100)
        .description("Final grade")
        .createdAt(LocalDateTime.now())
        .build();

    // When & Then
    assertThat(grade1).isEqualTo(grade2);
    assertThat(grade1.hashCode()).isEqualTo(grade2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    Grade grade = Grade.builder()
        .gradeId(1L)
        .assignedClass(assignedClass)
        .student(student)
        .teacher(teacher)
        .grade(85)
        .wage(100)
        .description("Final grade")
        .createdAt(LocalDateTime.now())
        .build();

    // When & Then
    String toString = grade.toString();
    assertThat(toString).contains("gradeId=1", "grade=85", "wage=100", "description=Final grade");
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    Grade grade = new Grade();

    // When & Then
    assertThat(grade.getGradeId()).isNull();
    assertThat(grade.getAssignedClass()).isNull();
    assertThat(grade.getStudent()).isNull();
    assertThat(grade.getTeacher()).isNull();
    assertThat(grade.getGrade()).isNull();
    assertThat(grade.getWage()).isNull();
    assertThat(grade.getDescription()).isNull();
    assertThat(grade.getCreatedAt()).isNull();
  }

  @Test
  void shouldHandleNullValuesForBuilder() {
    // Given
    Grade grade = Grade.builder().build();

    // When & Then
    assertThat(grade.getGradeId()).isNull();
    assertThat(grade.getAssignedClass()).isNull();
    assertThat(grade.getStudent()).isNull();
    assertThat(grade.getTeacher()).isNull();
    assertThat(grade.getGrade()).isNull();
    assertThat(grade.getWage()).isNull();
    assertThat(grade.getDescription()).isNull();
    assertThat(grade.getCreatedAt()).isNull();
  }

  @Test
  void shouldTestPrePersist() {
    // Given
    Grade grade = new Grade();
    grade.setAssignedClass(assignedClass);
    grade.setStudent(student);
    grade.setTeacher(teacher);
    grade.setGrade(85);
    grade.setWage(100);
    grade.setDescription("Final grade");

    // When: Trigger the persistence lifecycle event
    grade.onCreate(); // This will set the createdAt field

    // Then
    assertThat(grade.getCreatedAt()).isNotNull();
    assertThat(grade.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
  }
}
