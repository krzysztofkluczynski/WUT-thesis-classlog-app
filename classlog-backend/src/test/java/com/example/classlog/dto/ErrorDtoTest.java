package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ErrorDtoTest {

  @Test
  void shouldCreateErrorDto() {
    // Given
    String message = "An error occurred";

    // When
    ErrorDto errorDto = new ErrorDto(message);

    // Then
    assertThat(errorDto.message()).isEqualTo(message);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    String message = "An error occurred";

    ErrorDto errorDto1 = new ErrorDto(message);
    ErrorDto errorDto2 = new ErrorDto(message);

    // When & Then
    assertThat(errorDto1).isEqualTo(errorDto2);
    assertThat(errorDto1.hashCode()).isEqualTo(errorDto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    String message = "An error occurred";

    // When
    ErrorDto errorDto = new ErrorDto(message);

    // Then
    assertThat(errorDto.toString()).contains("message=" + message);
  }
}
