package com.example.classlog.controller;

import com.example.classlog.dto.GradeDto;
import com.example.classlog.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<GradeDto>> getGradesByClassId(@PathVariable Long classId) {
        List<GradeDto> grades = gradeService.findGradesByClassId(classId);
        return ResponseEntity.ok(grades);
    }

    @PostMapping
    public ResponseEntity<GradeDto> createGrade(@RequestBody GradeDto gradeDto) {
        GradeDto createdGrade = gradeService.saveGrade(gradeDto);
        return ResponseEntity.status(201).body(createdGrade);
    }
}
