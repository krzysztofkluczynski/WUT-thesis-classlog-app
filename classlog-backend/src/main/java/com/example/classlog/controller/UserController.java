package com.example.classlog.controller;

import com.example.classlog.dto.ChangePasswordDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/role/{roleId}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable Long roleId) {
        List<UserDto> usersWithRole = userService.findByRole(roleId);
        return ResponseEntity.ok(usersWithRole);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        UserDto userDto = userService.findUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/users/change-password")
    public ResponseEntity<UserDto> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        UserDto updatedUser = userService.changePassword(changePasswordDto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/users/class/{classId}")
    public ResponseEntity<List<UserDto>> getUsersByClass(@PathVariable long classId) {
        List<UserDto> users = userService.getUsersByClass(classId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/class/{classId}/role/{roleId}")
    public ResponseEntity<List<UserDto>> getUsersByClassAndRole(
            @PathVariable long classId,
            @PathVariable long roleId) {
        List<UserDto> users = userService.getUsersByClass(classId);

        List<UserDto> filteredUsers = users.stream()
                .filter(user -> user.getRole() != null && user.getRole().getId() == roleId)
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredUsers);
    }

}
