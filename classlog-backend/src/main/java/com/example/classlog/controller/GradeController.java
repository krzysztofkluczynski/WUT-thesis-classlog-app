package com.example.classlog.controller;

import com.example.classlog.dto.GradeDto;
import com.example.classlog.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/grades")
public class GradeController {

    private final GradeService gradeService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GradeDto>> getGradesByUserId(@PathVariable Long userId) {
        List<GradeDto> grades = gradeService.findGradesByUserId(userId);
        return ResponseEntity.ok(grades);
    }
}
