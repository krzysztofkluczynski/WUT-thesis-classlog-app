package com.example.classlog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.entity.Class;
import com.example.classlog.entity.Lesson;
import com.example.classlog.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LessonRepositoryTest {

  @Autowired
  private LessonRepository lessonRepository;

  @Autowired
  private ClassRepository classRepository;

  @Autowired
  private UserRepository userRepository;

  private Class classEntity;
  private User teacher;

  @BeforeEach
  void setUp() {
    // Given: Initialize test data
    classEntity = classRepository.save(
        Class.builder()
            .name("Test Class")
            .description("This is a test class.")
            .code("TEST123")
            .build()
    );

    teacher = userRepository.save(
        User.builder()
            .name("Jane")
            .surname("Doe")
            .email("teacher@example.com")
            .password("securepassword")
            .build()
    );

    lessonRepository.save(
        Lesson.builder()
            .classEntity(classEntity)
            .createdBy(teacher)
            .lessonDate(LocalDateTime.now())
            .subject("Sample Subject")
            .content("Sample Content")
            .build()
    );
  }

  @Test
  void shouldFindLessonsByClassId() {
    // When
    List<Lesson> lessons = lessonRepository.findByClassEntity_Id(classEntity.getId());

    // Then
    assertThat(lessons).hasSize(1);
    assertThat(lessons.get(0).getClassEntity().getName()).isEqualTo("Test Class");
    assertThat(lessons.get(0).getSubject()).isEqualTo("Sample Subject");
  }

  @Test
  void shouldFindLessonsByTeacherId() {
    // When
    List<Lesson> lessons = lessonRepository.findByCreatedBy_IdOrderByLessonDateDesc(
        teacher.getId());

    // Then
    assertThat(lessons).hasSize(1);
    assertThat(lessons.get(0).getCreatedBy().getEmail()).isEqualTo("teacher@example.com");
    assertThat(lessons.get(0).getLessonDate()).isNotNull();
  }
}
