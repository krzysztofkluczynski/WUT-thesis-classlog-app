package com.example.classlog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.GradeDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.service.GradeService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class GradeControllerTest {

  @Mock
  private GradeService gradeService;

  @InjectMocks
  private GradeController gradeController;

  private GradeDto gradeDto;
  private ClassDto classDto;
  private UserDto student;
  private UserDto teacher;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    classDto = ClassDto.builder()
        .id(1L)
        .name("Test Class")
        .description("Sample Class Description")
        .build();

    student = UserDto.builder()
        .id(2L)
        .email("student@example.com")
        .build();

    teacher = UserDto.builder()
        .id(3L)
        .email("teacher@example.com")
        .build();

    gradeDto = GradeDto.builder()
        .gradeId(1L)
        .assignedClass(classDto)
        .student(student)
        .teacher(teacher)
        .grade(95)
        .wage(10)
        .description("Excellent performance")
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Test
  void getGradesByUserId() {
    // Given
    List<GradeDto> grades = Arrays.asList(gradeDto, gradeDto);
    Mockito.when(gradeService.findGradesByUserId(2L)).thenReturn(grades);

    // When
    ResponseEntity<List<GradeDto>> response = gradeController.getGradesByUserId(2L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
    assertEquals(gradeDto.getGradeId(), response.getBody().get(0).getGradeId());
  }

  @Test
  void getGradesByClassId() {
    // Given
    List<GradeDto> grades = Arrays.asList(gradeDto, gradeDto);
    Mockito.when(gradeService.findGradesByClassId(1L)).thenReturn(grades);

    // When
    ResponseEntity<List<GradeDto>> response = gradeController.getGradesByClassId(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
    assertEquals(gradeDto.getAssignedClass().getId(),
        response.getBody().get(0).getAssignedClass().getId());
  }

  @Test
  void createGrade() {
    // Given
    Mockito.when(gradeService.saveGrade(any(GradeDto.class))).thenReturn(gradeDto);

    // When
    ResponseEntity<GradeDto> response = gradeController.createGrade(gradeDto);

    // Then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(gradeDto.getGradeId(), response.getBody().getGradeId());
  }

  @Test
  void deleteGrade() {
    // When
    ResponseEntity<Void> response = gradeController.deleteGrade(1L);

    // Then
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  void getGradesByUserIdEmpty() {
    // Given
    Mockito.when(gradeService.findGradesByUserId(2L)).thenReturn(Collections.emptyList());

    // When
    ResponseEntity<List<GradeDto>> response = gradeController.getGradesByUserId(2L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, response.getBody().size());
  }

  @Test
  void getGradesByClassIdEmpty() {
    // Given
    Mockito.when(gradeService.findGradesByClassId(1L)).thenReturn(Collections.emptyList());

    // When
    ResponseEntity<List<GradeDto>> response = gradeController.getGradesByClassId(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, response.getBody().size());
  }
}
