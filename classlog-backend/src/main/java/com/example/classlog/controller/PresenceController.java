package com.example.classlog.controller;


import com.example.classlog.dto.LessonPresenceDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.service.PresenceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/presence")
public class PresenceController {

  private final PresenceService presenceService;

  @PostMapping("/add/students")
  public ResponseEntity<String> addUsersToClass(@RequestBody LessonPresenceDto request) {
    presenceService.addPresenceForListOfStudents(request.getLessonId(), request.getUsers());
    return ResponseEntity.ok("Presence submitted.");
  }

  @GetMapping("/students/{lessonId}")
  public ResponseEntity<List<UserDto>> getPresentStudents(@PathVariable Long lessonId) {
    List<UserDto> students = presenceService.getPresentStudents(lessonId);
    return ResponseEntity.ok(students);
  }

  @PutMapping("/update/{lessonId}")
  public ResponseEntity<String> updateLessonPresence(@PathVariable Long lessonId,
      @RequestBody List<Long> presentStudents) {
    presenceService.updatePresenceForLesson(lessonId, presentStudents);
    return ResponseEntity.ok("Presence updated.");
  }

}
