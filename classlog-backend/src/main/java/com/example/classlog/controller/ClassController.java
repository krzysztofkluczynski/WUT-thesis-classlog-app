package com.example.classlog.controller;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/classes")
public class ClassController {

    @Autowired
    private final ClassService classService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ClassDto>> getClassesByUserId(@PathVariable Long userId) {
        List<ClassDto> classes = classService.findClassesByUserId(userId);
        return ResponseEntity.ok(classes);
    }
}