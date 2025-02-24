package com.example.classlog.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final UserAuthenticationProvider userAuthenticationProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    String requestURI = request.getRequestURI();

    if ("/refresh-token".equals(requestURI)) {
      filterChain.doFilter(request, response);
      return;
    }

    if (header != null) {
      String[] authElements = header.split(" ");

      if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
        try {
          if ("GET".equals(request.getMethod())) {
            SecurityContextHolder.getContext().setAuthentication(
                userAuthenticationProvider.validateToken(authElements[1]));
          } else {
            SecurityContextHolder.getContext().setAuthentication(
                userAuthenticationProvider.validateTokenStrongly(authElements[1]));
          }
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
          response.setContentType("application/json");
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.getWriter()
              .write("{\"error\": \"Token has expired. Please refresh your session.\"}");
          return;
        } catch (RuntimeException e) {
          SecurityContextHolder.clearContext();
          throw e;

        }
      }
    }

    filterChain.doFilter(request, response);
  }

}