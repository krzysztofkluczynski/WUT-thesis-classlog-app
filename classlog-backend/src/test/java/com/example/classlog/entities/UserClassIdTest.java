package com.example.classlog.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserClassIdTest {

  private UserClassId userClassId1;
  private UserClassId userClassId2;

  @BeforeEach
  void setUp() {
    // Setting up test data for UserClassId
    userClassId1 = new UserClassId(1L, 101L);
    userClassId2 = new UserClassId(1L, 101L);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    // userClassId1 and userClassId2 are initialized in @BeforeEach

    // When & Then
    assertThat(userClassId1).isEqualTo(userClassId2);
    assertThat(userClassId1.hashCode()).isEqualTo(userClassId2.hashCode());
  }

  @Test
  void shouldTestEqualsAndHashCodeForDifferentValues() {
    // Given
    UserClassId userClassId3 = new UserClassId(2L, 102L);

    // When & Then
    assertThat(userClassId1).isNotEqualTo(userClassId3);
    assertThat(userClassId1.hashCode()).isNotEqualTo(userClassId3.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    UserClassId userClassId = new UserClassId(1L, 101L);

    // When & Then
    String toString = userClassId.toString();
    assertThat(toString).contains("userId=1", "classId=101");
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    UserClassId userClassId = new UserClassId();

    // When & Then
    assertThat(userClassId.getUserId()).isNull();
    assertThat(userClassId.getClassId()).isNull();
  }
}
