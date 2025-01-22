package com.example.classlog.controller;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.CreateClassDto;
import com.example.classlog.dto.UserClassWithCodeDto;
import com.example.classlog.dto.UsersClassDto;
import com.example.classlog.service.ClassService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<ClassDto> addClass(@RequestBody CreateClassDto CreateClassDto) {
    ClassDto savedClass = classService.addClass(CreateClassDto);
    return ResponseEntity.ok(savedClass);
  }

  @PostMapping("/delete/users")
  public ResponseEntity<String> deleteUsersFromClass(
      @RequestBody UsersClassDto request) {
    classService.removeUsersFromClass(request.getClassId(), request.getUsers());
    return ResponseEntity.ok("Users successfully removed from the class.");
  }

  @PostMapping("/add/users")
  public ResponseEntity<String> addUsersToClass(@RequestBody UsersClassDto request) {
    classService.addUsersToClass(request.getClassId(), request.getUsers());
    return ResponseEntity.ok("Users successfully added to the class.");
  }

  @PostMapping("/add/code")
  public ResponseEntity<String> addUsersToClass(@RequestBody UserClassWithCodeDto request) {
    classService.addUserToClassByCode(request.getClassCode(), request.getUser());
    return ResponseEntity.ok("Users successfully added to the class.");
  }

}
