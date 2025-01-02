package com.example.classlog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.entity.Class;
import com.example.classlog.entity.Grade;
import com.example.classlog.entity.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class GradeRepositoryTest {

  @Autowired
  private GradeRepository gradeRepository;

  @Autowired
  private ClassRepository classRepository;

  @Autowired
  private UserRepository userRepository;

  private Class classEntity;
  private User student;
  private User teacher;

  @BeforeEach
  void setUp() {
    // Given: Initialize test data
    classEntity = classRepository.save(
        Class.builder()
            .name("Test Class")
            .description("Description of Test Class")
            .code("TEST123")
            .build()
    );

    student = userRepository.save(
        User.builder()
            .name("John")
            .surname("Doe")
            .email("student@example.com")
            .password("password123")
            .build()
    );

    teacher = userRepository.save(
        User.builder()
            .name("Jane")
            .surname("Smith")
            .email("teacher@example.com")
            .password("password456")
            .build()
    );

    gradeRepository.save(
        Grade.builder()
            .assignedClass(classEntity)
            .student(student)
            .teacher(teacher)
            .grade(90)
            .wage(2)
            .description("Test Grade Description")
            .build()
    );
  }

  @Test
  void shouldFindGradesByStudentId() {
    // When
    List<Grade> grades = gradeRepository.findByStudent_Id(student.getId());

    // Then
    assertThat(grades).hasSize(1);
    assertThat(grades.get(0).getGrade()).isEqualTo(90);
    assertThat(grades.get(0).getDescription()).isEqualTo("Test Grade Description");
    assertThat(grades.get(0).getAssignedClass().getName()).isEqualTo("Test Class");
  }

  @Test
  void shouldFindGradesByClassId() {
    // When
    List<Grade> grades = gradeRepository.findByAssignedClass_IdOrderByCreatedAtDesc(
        classEntity.getId());

    // Then
    assertThat(grades).hasSize(1);
    assertThat(grades.get(0).getGrade()).isEqualTo(90);
    assertThat(grades.get(0).getStudent().getEmail()).isEqualTo("student@example.com");
  }
}
