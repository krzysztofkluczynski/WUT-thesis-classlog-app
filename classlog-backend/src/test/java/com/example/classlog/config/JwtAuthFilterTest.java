package com.example.classlog.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

class JwtAuthFilterTest {

  private JwtAuthFilter jwtAuthFilter;
  private UserAuthenticationProvider userAuthenticationProvider;
  private HttpServletRequest request;
  private HttpServletResponse response;
  private FilterChain filterChain;

  @BeforeEach
  void setUp() {
    userAuthenticationProvider = mock(UserAuthenticationProvider.class);
    jwtAuthFilter = new JwtAuthFilter(userAuthenticationProvider);
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    filterChain = mock(FilterChain.class);
  }

  @Test
  void doFilterInternal_WithValidToken_ShouldAuthenticate() throws ServletException, IOException {
    when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer valid-token");
    when(request.getMethod()).thenReturn("GET");
    when(userAuthenticationProvider.validateToken("valid-token")).thenReturn(null);
  }
}