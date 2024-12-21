package com.example.classlog;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ClassLogApplicationTest {

  @Value("${spring.datasource.url:NOT_SET}")
  private String datasourceUrl;

  @Value("${spring.profiles.active:NOT_SET}")
  private String activeProfile;

  @Test
  void contextLoads() {
    // Assert that the application context loads successfully
  }

  @Test
  void shouldInjectDatasourceUrl() {
    // Check if datasourceUrl property is injected correctly
    assertThat(datasourceUrl).isNotEqualTo("NOT_SET");
  }

  @Test
  void shouldInjectActiveProfile() {
    // Check if activeProfile property is injected correctly
    assertThat(activeProfile).isEqualTo("test");
  }

  @Test
  void mainMethodShouldRunWithoutErrors() {
    // Test the main method
    ClassLogApplication.main(new String[]{});
  }

  @Test
  void shouldNotFailWhenConnectingToDatabase() {
    if (!datasourceUrl.equals("NOT_SET")) {
      assertThat(datasourceUrl).contains("jdbc:h2:"); // Assuming H2 database for tests
    } else {
      throw new IllegalStateException("Datasource URL is not set correctly for test profile.");
    }
  }
}
