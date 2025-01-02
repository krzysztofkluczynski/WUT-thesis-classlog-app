package com.example.classlog.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserClassTest {

  private UserClassId userClassId;
  private User user;
  private Class classEntity;
  private UserClass userClass;

  @BeforeEach
  void setUp() {
    // Setting up test data for UserClass and related entities
    userClassId = new UserClassId(1L, 101L);
    user = User.builder().id(1L).name("John").surname("Doe").email("john.doe@example.com").build();
    classEntity = Class.builder().id(101L).name("Math 101").build();
    userClass = new UserClass(userClassId, user, classEntity);
  }

  @Test
  void shouldCreateUserClassUsingConstructor() {
    // Given
    Long userId = 1L;
    Long classId = 101L;
    UserClassId id = new UserClassId(userId, classId);

    // When
    UserClass userClass = new UserClass(id, user, classEntity);

    // Then
    assertThat(userClass.getId()).isEqualTo(id);
    assertThat(userClass.getUser()).isEqualTo(user);
    assertThat(userClass.getClassEntity()).isEqualTo(classEntity);
  }

  @Test
  void shouldCreateUserClassUsingBuilder() {
    // Given
    Long userId = 1L;
    Long classId = 101L;
    UserClassId id = new UserClassId(userId, classId);

    // When
    UserClass userClass = UserClass.builder()
        .id(id)
        .user(user)
        .classEntity(classEntity)
        .build();

    // Then
    assertThat(userClass.getId()).isEqualTo(id);
    assertThat(userClass.getUser()).isEqualTo(user);
    assertThat(userClass.getClassEntity()).isEqualTo(classEntity);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    UserClass userClass = new UserClass();

    // When
    userClass.setId(userClassId);
    userClass.setUser(user);
    userClass.setClassEntity(classEntity);

    // Then
    assertThat(userClass.getId()).isEqualTo(userClassId);
    assertThat(userClass.getUser()).isEqualTo(user);
    assertThat(userClass.getClassEntity()).isEqualTo(classEntity);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    UserClass userClass1 = new UserClass(userClassId, user, classEntity);
    UserClass userClass2 = new UserClass(userClassId, user, classEntity);

    // When & Then
    assertThat(userClass1).isEqualTo(userClass2);
    assertThat(userClass1.hashCode()).isEqualTo(userClass2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    UserClass userClass = new UserClass(userClassId, user, classEntity);

    // When & Then
    String toString = userClass.toString();
    assertThat(toString).contains("id=" + userClassId, "user=" + user,
        "classEntity=" + classEntity);
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    UserClass userClass = new UserClass();

    // When & Then
    assertThat(userClass.getId()).isNull();
    assertThat(userClass.getUser()).isNull();
    assertThat(userClass.getClassEntity()).isNull();
  }

  @Test
  void shouldHandleNullValuesForBuilder() {
    // Given
    UserClass userClass = UserClass.builder().build();

    // When & Then
    assertThat(userClass.getId()).isNull();
    assertThat(userClass.getUser()).isNull();
    assertThat(userClass.getClassEntity()).isNull();
  }
}
