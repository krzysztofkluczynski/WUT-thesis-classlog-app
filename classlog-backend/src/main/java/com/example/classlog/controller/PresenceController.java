package com.example.classlog.controller;


import com.example.classlog.dto.LessonPresenceRequest;
import com.example.classlog.dto.ManageUserClassWithCodeDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/presence")
public class PresenceController {

    private final PresenceService presenceService;

    @PostMapping("/add/students")
    public ResponseEntity<String> addUsersToClass(@RequestBody LessonPresenceRequest request) {
        presenceService.addPresenceForListOfStudents(request.getLessonId(), request.getUsers());
        return ResponseEntity.ok("Presence submitted.");
    }

    @GetMapping("/students/{lessonId}")
    public ResponseEntity<List<UserDto>> getPresentStudents(@PathVariable Long lessonId) {
        List<UserDto> students = presenceService.getPresentStudents(lessonId);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/update/{lessonId}")
    public ResponseEntity<String> updateLessonPresence(@PathVariable Long lessonId, @RequestBody List<Long> presentStudents) {
        presenceService.updatePresenceForLesson(lessonId, presentStudents);
        return ResponseEntity.ok("Presence updated.");
    }

}
