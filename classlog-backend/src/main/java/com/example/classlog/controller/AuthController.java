package com.example.classlog.controller;

import com.example.classlog.config.UserAuthenticationProvider;
import com.example.classlog.dto.CredentialsDto;
import com.example.classlog.dto.SignUpDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.service.UserService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final UserAuthenticationProvider userAuthenticationProvider;

  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
    UserDto user = userService.login(credentialsDto);
    user.setToken(userAuthenticationProvider.createToken(user));
    return ResponseEntity.ok(user);
  }

  @PostMapping("/register")
  public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
    UserDto user = userService.register(signUpDto);
    return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<UserDto> refreshAccessToken(@RequestBody UserDto userDto) {
    UserDto user = userAuthenticationProvider.refreshToken(userDto);
    return ResponseEntity.ok(user);
  }

}
