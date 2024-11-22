package com.example.classlog.controller;


import com.example.classlog.dto.LessonPresenceRequest;
import com.example.classlog.dto.ManageUserClassWithCodeDto;
import com.example.classlog.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
