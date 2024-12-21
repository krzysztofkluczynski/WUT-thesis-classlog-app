package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CredentialsDtoTest {

  @Test
  void shouldCreateCredentialsDto() {
    // Given
    String email = "john.doe@example.com";
    char[] password = new char[]{'s', 'e', 'c', 'r', 'e', 't'};

    // When
    CredentialsDto credentialsDto = new CredentialsDto(email, password);

    // Then
    assertThat(credentialsDto.email()).isEqualTo(email);
    assertThat(credentialsDto.password()).isEqualTo(password);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    String email = "john.doe@example.com";
    char[] password = new char[]{'s', 'e', 'c', 'r', 'e', 't'};

    CredentialsDto credentialsDto1 = new CredentialsDto(email, password);
    CredentialsDto credentialsDto2 = new CredentialsDto(email, password);

    // When & Then
    assertThat(credentialsDto1).isEqualTo(credentialsDto2);
    assertThat(credentialsDto1.hashCode()).isEqualTo(credentialsDto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    String email = "john.doe@example.com";
    char[] password = new char[]{'s', 'e', 'c', 'r', 'e', 't'};

    CredentialsDto credentialsDto = new CredentialsDto(email, password);

    // When & Then
    String toString = credentialsDto.toString();
    assertThat(toString).contains("email=" + email);
  }

  @Test
  void shouldHandleNullPassword() {
    // Given
    String email = "john.doe@example.com";
    char[] password = null;

    // When
    CredentialsDto credentialsDto = new CredentialsDto(email, password);

    // Then
    assertThat(credentialsDto.email()).isEqualTo(email);
    assertThat(credentialsDto.password()).isNull();
  }

  @Test
  void shouldHandleEmptyPassword() {
    // Given
    String email = "john.doe@example.com";
    char[] password = new char[0];  // Empty password

    // When
    CredentialsDto credentialsDto = new CredentialsDto(email, password);

    // Then
    assertThat(credentialsDto.email()).isEqualTo(email);
    assertThat(credentialsDto.password()).isEmpty();
  }
}
