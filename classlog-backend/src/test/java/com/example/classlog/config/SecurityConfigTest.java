package com.example.classlog.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SecurityConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldRestrictAccessToProtectedEndpointsForUnauthorizedUsers() throws Exception {
    mockMvc.perform(get("/protected-endpoint"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldReturnUnauthorizedForMissingAuth() throws Exception {
    mockMvc.perform(get("/protected-endpoint"))
        .andExpect(status().isUnauthorized());
  }
}
