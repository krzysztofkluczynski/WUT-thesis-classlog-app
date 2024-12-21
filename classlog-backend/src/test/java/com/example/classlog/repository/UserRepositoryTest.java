package com.example.classlog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.entities.Class;
import com.example.classlog.entities.User;
import com.example.classlog.entities.UserClass;
import com.example.classlog.entities.UserClassId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ClassRepository classRepository;

  @Autowired
  private UserClassRepository userClassRepository;

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
  void shouldFindUsersByClassId() {
    // When
    List<User> users = userRepository.findByClassId(classEntity.getId());

    // Then
    assertThat(users).hasSize(1);
    assertThat(users.get(0).getEmail()).isEqualTo("john.doe@example.com");
  }

  @Test
  void shouldFindUsersNotFromClass() {
    // Given
    User otherUser = userRepository.save(User.builder()
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .password("password")
        .build());

    // When
    List<User> users = userRepository.findUsersNotFromClass(classEntity.getId());

    // Then
    assertThat(users).hasSize(1);
    assertThat(users.get(0).getEmail()).isEqualTo("jane.smith@example.com");
  }
}
