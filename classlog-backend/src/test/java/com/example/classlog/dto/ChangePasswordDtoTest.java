package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ChangePasswordDtoTest {

  @Test
  void shouldCreateChangePasswordDtoUsingBuilder() {
    // Given
    long userId = 1L;
    String oldPassword = "oldPassword123";
    String newPassword = "newPassword456";

    // When
    ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
        .userId(userId)
        .oldPassword(oldPassword)
        .newPassword(newPassword)
        .build();

    // Then
    assertThat(changePasswordDto.getUserId()).isEqualTo(userId);
    assertThat(changePasswordDto.getOldPassword()).isEqualTo(oldPassword);
    assertThat(changePasswordDto.getNewPassword()).isEqualTo(newPassword);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    ChangePasswordDto changePasswordDto = ChangePasswordDto.builder().build();

    // When
    changePasswordDto.setUserId(1L);
    changePasswordDto.setOldPassword("oldPassword123");
    changePasswordDto.setNewPassword("newPassword456");

    // Then
    assertThat(changePasswordDto.getUserId()).isEqualTo(1L);
    assertThat(changePasswordDto.getOldPassword()).isEqualTo("oldPassword123");
    assertThat(changePasswordDto.getNewPassword()).isEqualTo("newPassword456");
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    ChangePasswordDto changePasswordDto1 = ChangePasswordDto.builder()
        .userId(1L)
        .oldPassword("oldPassword123")
        .newPassword("newPassword456")
        .build();

    ChangePasswordDto changePasswordDto2 = ChangePasswordDto.builder()
        .userId(1L)
        .oldPassword("oldPassword123")
        .newPassword("newPassword456")
        .build();

    // When & Then
    assertThat(changePasswordDto1).isEqualTo(changePasswordDto2);
    assertThat(changePasswordDto1.hashCode()).isEqualTo(changePasswordDto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
        .userId(1L)
        .oldPassword("oldPassword123")
        .newPassword("newPassword456")
        .build();

    // When & Then
    assertThat(changePasswordDto.toString()).contains("userId=1", "oldPassword=oldPassword123",
        "newPassword=newPassword456");
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    ChangePasswordDto changePasswordDto = ChangePasswordDto.builder().build();

    // When & Then
    assertThat(changePasswordDto.getUserId()).isEqualTo(0L);
    assertThat(changePasswordDto.getOldPassword()).isNull();
    assertThat(changePasswordDto.getNewPassword()).isNull();
  }
}
