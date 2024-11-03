package com.example.classlog.controller;

import com.example.classlog.Repository.UserRepository;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.User;
import com.example.classlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable Long roleId) {
        List<UserDto> usersWithRole = userService.findByRole(roleId); // Assume findUsersByRole(role) is implemented in UserService
        return ResponseEntity.ok(usersWithRole);
    }
}
