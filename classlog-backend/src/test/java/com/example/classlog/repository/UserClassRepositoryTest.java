package com.example.classlog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.entities.Class;
import com.example.classlog.entities.User;
import com.example.classlog.entities.UserClass;
import com.example.classlog.entities.UserClassId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserClassRepositoryTest {

  @Autowired
  private UserClassRepository userClassRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ClassRepository classRepository;

  private User user;
  private Class classEntity;

  @BeforeEach
  void setUp() {
    // Given
    user = userRepository.save(User.builder()
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .password("password")
        .build());

    classEntity = classRepository.save(Class.builder()
        .name("Test Class")
        .build());

    userClassRepository.save(new UserClass(
        new UserClassId(user.getId(), classEntity.getId()),
        user,
        classEntity
    ));
  }

  @Test
  void shouldCheckIfUserExistsInClass() {
    // When
    boolean exists = userClassRepository.existsByClassEntity_IdAndUser_Id(
        classEntity.getId(), user.getId());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void shouldInsertUserIntoClass() {
    // Given
    Long newUserId = userRepository.save(User.builder()
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .password("password")
        .build()).getId();

    // When
    userClassRepository.insertUserIntoClass(classEntity.getId(), newUserId);

    // Then
    boolean exists = userClassRepository.existsByClassEntity_IdAndUser_Id(
        classEntity.getId(), newUserId);
    assertThat(exists).isTrue();
  }

  @Test
  void shouldDeleteUserFromClass() {
    // When
    userClassRepository.deleteUserFromClass(classEntity.getId(), user.getId());

    // Then
    boolean exists = userClassRepository.existsByClassEntity_IdAndUser_Id(
        classEntity.getId(), user.getId());
    assertThat(exists).isFalse();
  }
}
