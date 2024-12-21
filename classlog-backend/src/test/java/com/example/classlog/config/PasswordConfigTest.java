package com.example.classlog.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordConfigTest {

  @Test
  void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
        PasswordConfig.class);
    PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

    assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
  }
}