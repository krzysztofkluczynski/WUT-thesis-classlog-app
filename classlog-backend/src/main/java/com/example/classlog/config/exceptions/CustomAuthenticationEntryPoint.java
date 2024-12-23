package com.example.classlog.config.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    response.setContentType("application/json");

    // Check the cause of the exception
    Throwable cause = authException.getCause();
    System.out.println("Cause" + cause.getClass());
    if (cause instanceof com.auth0.jwt.exceptions.TokenExpiredException) {
      // Token expired
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter()
          .write("{\"error\": \"Token has expired. Please refresh your session.\"}");
    } else {
      // Generic unauthorized response
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write("{\"error\": \"Unauthorized access. Please log in.\"}");
    }
  }
}
