package com.example.classlog.config.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {
    Throwable cause = accessDeniedException.getCause();

    response.setContentType("application/json");

    // Check if the exception was caused by a token expiration
    if (cause instanceof com.auth0.jwt.exceptions.TokenExpiredException) {
      // Token expired
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter()
          .write("{\"error\": \"Token has expired. Please refresh your session.\"}");
    } else {
      // Generic unauthorized response
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write("{\"error\": \"Unauthorized access.\"}");
    }
  }
}
