package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SignUpDtoTest {

  @Test
  void shouldCreateSignUpDtoUsingConstructor() {
    // Given
    String name = "John";
    String surname = "Doe";
    String email = "john.doe@example.com";
    String password = "password123";

    // When
    SignUpDto signUpDto = new SignUpDto(name, surname, email, password);

    // Then
    assertThat(signUpDto.name()).isEqualTo(name);
    assertThat(signUpDto.surname()).isEqualTo(surname);
    assertThat(signUpDto.email()).isEqualTo(email);
    assertThat(signUpDto.password()).isEqualTo(password);
  }

  @Test
  void shouldTestGetters() {
    // Given
    String name = "John";
    String surname = "Doe";
    String email = "john.doe@example.com";
    String password = "password123";

    SignUpDto signUpDto = new SignUpDto(name, surname, email, password);

    // Then
    assertThat(signUpDto.name()).isEqualTo(name);
    assertThat(signUpDto.surname()).isEqualTo(surname);
    assertThat(signUpDto.email()).isEqualTo(email);
    assertThat(signUpDto.password()).isEqualTo(password);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    String name = "John";
    String surname = "Doe";
    String email = "john.doe@example.com";
    String password = "password123";

    SignUpDto signUpDto1 = new SignUpDto(name, surname, email, password);
    SignUpDto signUpDto2 = new SignUpDto(name, surname, email, password);

    // When & Then
    assertThat(signUpDto1).isEqualTo(signUpDto2);
    assertThat(signUpDto1.hashCode()).isEqualTo(signUpDto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    String name = "John";
    String surname = "Doe";
    String email = "john.doe@example.com";
    String password = "password123";

    SignUpDto signUpDto = new SignUpDto(name, surname, email, password);

    // When & Then
    String toString = signUpDto.toString();
    assertThat(toString).contains("name=" + name, "surname=" + surname, "email=" + email,
        "password=" + password);
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    SignUpDto signUpDto = new SignUpDto(null, null, null, null);

    // When & Then
    assertThat(signUpDto.name()).isNull();
    assertThat(signUpDto.surname()).isNull();
    assertThat(signUpDto.email()).isNull();
    assertThat(signUpDto.password()).isNull();
  }
}
