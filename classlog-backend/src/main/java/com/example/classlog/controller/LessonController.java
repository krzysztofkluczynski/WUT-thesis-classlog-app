package com.example.classlog.controller;

import com.example.classlog.dto.LessonDto;
import com.example.classlog.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<LessonDto>> getAllLessons() {
        List<LessonDto> lessons = lessonService.getAllLessons();
        if (lessons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable Long id) {
        LessonDto lesson = lessonService.getLessonById(id);
        if (lesson == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lesson);
    }

    @PostMapping
    public ResponseEntity<LessonDto> createLesson(@RequestBody LessonDto lessonDto) {
        LessonDto createdLesson = lessonService.createLesson(lessonDto);
        return ResponseEntity.ok(createdLesson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/createdBy/{userId}/recent/{numbOfLessons}")
    public ResponseEntity<List<LessonDto>> getRecentLessonsCreatedBy(
            @PathVariable Long userId,
            @PathVariable int numbOfLessons) {
        List<LessonDto> lessons = lessonService.getRecentLessonsCreatedBy(userId, numbOfLessons);
        if (lessons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/user/createdBy//{userId}")
    public ResponseEntity<List<LessonDto>> getAllLessonsCreatedBy(@PathVariable Long userId) {
        List<LessonDto> lessons = lessonService.getAllLessonsCreatedBy(userId);
        if (lessons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LessonDto>> getAllLessonsForUser(@PathVariable Long userId) {
        List<LessonDto> lessons = lessonService.getAllLessonsForUser(userId);
        if (lessons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/user/{userId}/recent/{numbOfLessons}")
    public ResponseEntity<List<LessonDto>> getRecentLessonsForUser(
            @PathVariable Long userId,
            @PathVariable int numbOfLessons) {
        List<LessonDto> lessons = lessonService.getRecentLessonsForUser(userId, numbOfLessons);
        if (lessons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<LessonDto>> GetLessonsByClassId(@PathVariable Long classId) {
        List<LessonDto> lessons = lessonService.getLessonByClassId(classId);
        if (lessons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/class/{classId}/recent/{numbOfLessons}")
    public ResponseEntity<List<LessonDto>> GetRecentLessonsByClassId(
            @PathVariable Long classId,
            @PathVariable int numbOfLessons) {
        List<LessonDto> lessons = lessonService.getRecentLessonsByClassId(classId, numbOfLessons);
        if (lessons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lessons);
    }
}
