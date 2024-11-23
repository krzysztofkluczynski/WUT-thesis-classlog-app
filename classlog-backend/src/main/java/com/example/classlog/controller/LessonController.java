package com.example.classlog.controller;

import com.example.classlog.dto.LessonDto;
import com.example.classlog.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<LessonDto>> getAllLessons() {
        List<LessonDto> lessons = lessonService.getAllLessons();
        if (lessons.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable Long id) {
        System.out.println("Received ID in Controller: " + id);

        if (id == null) {
            System.out.println("Received null ID");
            return ResponseEntity.badRequest().build();
        }

        LessonDto lesson = lessonService.getLessonById(id);

        System.out.println("Lesson returned from service: " + lesson);
        return ResponseEntity.ok(lesson);
    }

    @PostMapping
    public ResponseEntity<LessonDto> createLesson(@RequestBody LessonDto lessonDto) {
        LessonDto createdLesson = lessonService.createLesson(lessonDto);
        return ResponseEntity.ok(createdLesson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok("Lesson deleted.");
    }

    @GetMapping("/user/createdBy/{userId}/recent/{numbOfLessons}")
    public ResponseEntity<List<LessonDto>> getRecentLessonsCreatedBy(
            @PathVariable Long userId,
            @PathVariable int numbOfLessons) {
        List<LessonDto> lessons = lessonService.getRecentLessonsCreatedBy(userId, numbOfLessons);
        if (lessons.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/user/createdBy//{userId}")
    public ResponseEntity<List<LessonDto>> getAllLessonsCreatedBy(@PathVariable Long userId) {
        List<LessonDto> lessons = lessonService.getAllLessonsCreatedBy(userId);
        if (lessons.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LessonDto>> getAllLessonsForUser(@PathVariable Long userId) {
        List<LessonDto> lessons = lessonService.getAllLessonsForUser(userId);
        if (lessons.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/user/{userId}/recent/{numbOfLessons}")
    public ResponseEntity<List<LessonDto>> getRecentLessonsForUser(
            @PathVariable Long userId,
            @PathVariable int numbOfLessons) {
        List<LessonDto> lessons = lessonService.getRecentLessonsForUser(userId, numbOfLessons);
        if (lessons.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<LessonDto>> GetLessonsByClassId(@PathVariable Long classId) {
        List<LessonDto> lessons = lessonService.getLessonByClassId(classId);
        if (lessons.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/class/{classId}/recent/{numbOfLessons}")
    public ResponseEntity<List<LessonDto>> GetRecentLessonsByClassId(
            @PathVariable Long classId,
            @PathVariable int numbOfLessons) {
        List<LessonDto> lessons = lessonService.getRecentLessonsByClassId(classId, numbOfLessons);
        if (lessons.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(lessons);
    }

    @PutMapping("/update")
    public ResponseEntity<LessonDto> updateLesson(@RequestBody LessonDto lessonDto) {
        LessonDto updatedLesson = lessonService.updateLesson(lessonDto);
        return ResponseEntity.ok(updatedLesson);
    }
}
