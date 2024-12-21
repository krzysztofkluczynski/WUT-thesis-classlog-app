package com.example.classlog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.entities.Class;
import com.example.classlog.entities.Lesson;
import com.example.classlog.entities.Presence;
import com.example.classlog.entities.User;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class PresenceRepositoryTest {

  @Autowired
  private PresenceRepository presenceRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ClassRepository classRepository;

  @Autowired
  private LessonRepository lessonRepository;

  private Lesson lesson;
  private User student;

  @BeforeEach
  void setUp() {
    // Given
    Class classEntity = classRepository.save(
        Class.builder()
            .name("Test Class")
            .description("A test class")
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

    lesson = lessonRepository.save(
        Lesson.builder()
            .classEntity(classEntity)
            .createdBy(student)
            .lessonDate(LocalDateTime.now())
            .subject("Math")
            .content("Math Lesson Content")
            .build()
    );

    presenceRepository.save(
        Presence.builder()
            .lesson(lesson)
            .student(student)
            .build()
    );
  }

  @Test
  void shouldCheckIfPresenceExists() {
    // When
    boolean exists = presenceRepository.existsByLesson_LessonIdAndStudent_Id(lesson.getLessonId(),
        student.getId());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  @Transactional
  void shouldInsertPresence() {
    // Given
    User newStudent = userRepository.save(
        User.builder()
            .name("Jane")
            .surname("Smith")
            .email("jane@example.com")
            .password("password456")
            .build()
    );

    // When
    presenceRepository.insertPresence(lesson.getLessonId(), newStudent.getId());

    // Then
    boolean exists = presenceRepository.existsByLesson_LessonIdAndStudent_Id(lesson.getLessonId(),
        newStudent.getId());
    assertThat(exists).isTrue();
  }

  @Test
  void shouldFindUsersByLessonId() {
    // When
    List<User> users = presenceRepository.findUsersByLessonId(lesson.getLessonId());

    // Then
    assertThat(users).hasSize(1);
    assertThat(users.get(0).getEmail()).isEqualTo("student@example.com");
  }

  @Test
  void shouldFindStudentIdsByLessonId() {
    // When
    List<Long> studentIds = presenceRepository.findStudentIdsByLesson_LessonId(
        lesson.getLessonId());

    // Then
    assertThat(studentIds).containsExactly(student.getId());
  }

  @Test
  void shouldDeleteByLessonIdAndStudentId() {
    // When
    presenceRepository.deleteByLesson_LessonIdAndStudent_Id(lesson.getLessonId(), student.getId());
    boolean exists = presenceRepository.existsByLesson_LessonIdAndStudent_Id(lesson.getLessonId(),
        student.getId());


  }
}