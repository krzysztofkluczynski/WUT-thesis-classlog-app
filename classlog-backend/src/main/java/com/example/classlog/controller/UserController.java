package com.example.classlog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class UserController {

    @GetMapping("/users")
    public ResponseEntity<List<String>> users() {
        return ResponseEntity.ok(Arrays.asList("Jan", "Krzysztof"));
    }
}
