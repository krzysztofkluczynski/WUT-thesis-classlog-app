package com.example.classlog.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.config.exceptions.CustomAccessDeniedHandler;
import com.example.classlog.config.exceptions.CustomAuthenticationEntryPoint;
import com.example.classlog.config.exceptions.RestExceptionHandler;
import com.example.classlog.dto.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootTest
class WebConfigTest {

  private WebConfig webConfig;

  @Value("${custom.cors.allowed-origin:http://localhost:4200}")
  private String allowedOrigin;

  @BeforeEach
  void setUp() {
    webConfig = new WebConfig();
    webConfig.allowedOrigin = allowedOrigin;
  }

  @Test
  void corsFilter_ShouldConfigureCorsCorrectly() {
    CorsFilter corsFilter = webConfig.corsFilter();
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowCredentials(true);
    config.addAllowedOrigin(allowedOrigin);
    config.setAllowedHeaders(Arrays.asList(
        HttpHeaders.AUTHORIZATION,
        HttpHeaders.CONTENT_TYPE,
        HttpHeaders.ACCEPT));
    config.setAllowedMethods(Arrays.asList(
        HttpMethod.GET.name(),
        HttpMethod.POST.name(),
        HttpMethod.PUT.name(),
        HttpMethod.DELETE.name()));

    source.registerCorsConfiguration("/**", config);

    assertNotNull(config);
    assertTrue(config.getAllowCredentials());
    assertEquals(
        Arrays.asList(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT),
        config.getAllowedHeaders());
    assertEquals(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
        HttpMethod.DELETE.name()), config.getAllowedMethods());
    assertEquals(allowedOrigin, config.getAllowedOrigins().get(0));
  }

  @Test
  void accessDeniedHandler_ShouldThrowAppException() {
    CustomAccessDeniedHandler handler = new CustomAccessDeniedHandler();
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();

    Exception exception = assertThrows(AppException.class, () ->
        handler.handle(request, response,
            new org.springframework.security.access.AccessDeniedException("Access Denied"))
    );

    assertEquals("You do not have permission to access this resource.", exception.getMessage());
    assertEquals(HttpStatus.FORBIDDEN, ((AppException) exception).getStatus());
  }

  @Test
  void authenticationEntryPoint_ShouldRespondWithUnauthorized() throws IOException {
    CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();
    MockHttpServletResponse response = new MockHttpServletResponse();

    entryPoint.commence(new MockHttpServletRequest(), response,
        Mockito.mock(org.springframework.security.core.AuthenticationException.class));

    assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    assertEquals("application/json", response.getContentType());
    assertEquals("{\"error\": \"Unauthorized access. Please log in.\"}",
        response.getContentAsString());
  }

  @Test
  void restExceptionHandler_ShouldHandleAppException() {
    RestExceptionHandler handler = new RestExceptionHandler();
    AppException exception = new AppException("Test error", HttpStatus.BAD_REQUEST);

    ResponseEntity<ErrorDto> response = handler.handleAppException(exception);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
  }
}