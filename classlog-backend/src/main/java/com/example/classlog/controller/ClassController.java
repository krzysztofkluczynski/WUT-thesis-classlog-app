package com.example.classlog.controller;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.ManageUserClassRequestDto;
import com.example.classlog.dto.ManageUserClassWithCodeDto;
import com.example.classlog.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/classes")
public class ClassController {

    @Autowired
    private final ClassService classService;
    @GetMapping("/{classId}")
    public ResponseEntity<ClassDto> getClassById(@PathVariable Long classId) {
        ClassDto classDto = classService.findClassById(classId);
        return ResponseEntity.ok(classDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ClassDto>> getClassesByUserId(@PathVariable Long userId) {
        List<ClassDto> classes = classService.findClassesByUserId(userId);
        return ResponseEntity.ok(classes);
    }

    @PostMapping()
    public ResponseEntity<ClassDto> addClass(@RequestBody ClassDto classDto) {
        ClassDto savedClass = classService.addClass(classDto); // Ensure the service returns the saved class
        return ResponseEntity.ok(savedClass);
    }

    @PostMapping("/delete/users")
    public ResponseEntity<String> deleteUsersFromClass(@RequestBody ManageUserClassRequestDto request) {
        classService.removeUsersFromClass(request.getClassId(), request.getUsers());
        return ResponseEntity.ok("Users successfully removed from the class.");
    }

    @PostMapping("/add/users")
    public ResponseEntity<String> addUsersToClass(@RequestBody ManageUserClassRequestDto request) {
        classService.addUsersToClass(request.getClassId(), request.getUsers());
        return ResponseEntity.ok("Users successfully added to the class.");
    }

    @PostMapping("/add/code")
    public ResponseEntity<String> addUsersToClass(@RequestBody ManageUserClassWithCodeDto request) {
        classService.addUserToClassByCode(request.getClassCode(), request.getUser());
        return ResponseEntity.ok("Users successfully added to the class.");
    }

}
