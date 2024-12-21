package com.example.classlog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.LessonDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.service.LessonService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class LessonControllerTest {

  @Mock
  private LessonService lessonService;

  @InjectMocks
  private LessonController lessonController;

  private LessonDto lessonDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    UserDto userDto = UserDto.builder()
        .id(1L)
        .email("user@example.com")
        .build();

    ClassDto classDto = ClassDto.builder()
        .id(1L)
        .name("Sample Class")
        .description("Sample Description")
        .build();

    lessonDto = LessonDto.builder()
        .lessonId(1L)
        .createdByUser(userDto)
        .lessonClass(classDto)
        .lessonDate(LocalDateTime.now())
        .subject("Sample Subject")
        .content("Sample Content")
        .build();
  }

  @Test
  void getAllLessons() {
    // Given
    List<LessonDto> lessons = Arrays.asList(lessonDto, lessonDto);
    Mockito.when(lessonService.getAllLessons()).thenReturn(lessons);

    // When
    ResponseEntity<List<LessonDto>> response = lessonController.getAllLessons();

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, Objects.requireNonNull(response.getBody()).size());
  }

  @Test
  void getLessonById() {
    // Given
    Mockito.when(lessonService.getLessonById(1L)).thenReturn(lessonDto);

    // When
    ResponseEntity<LessonDto> response = lessonController.getLessonById(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(lessonDto.getLessonId(), Objects.requireNonNull(response.getBody()).getLessonId());
  }

  @Test
  void createLesson() {
    // Given
    Mockito.when(lessonService.createLesson(any(LessonDto.class))).thenReturn(lessonDto);

    // When
    ResponseEntity<LessonDto> response = lessonController.createLesson(lessonDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(lessonDto.getLessonId(), Objects.requireNonNull(response.getBody()).getLessonId());
  }

  @Test
  void deleteLesson() {
    // When
    ResponseEntity<String> response = lessonController.deleteLesson(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Lesson deleted.", response.getBody());
  }

  @Test
  void getRecentLessonsCreatedBy() {
    // Given
    List<LessonDto> lessons = Arrays.asList(lessonDto, lessonDto);
    Mockito.when(lessonService.getRecentLessonsCreatedBy(1L, 2)).thenReturn(lessons);

    // When
    ResponseEntity<List<LessonDto>> response = lessonController.getRecentLessonsCreatedBy(1L, 2);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
  }

  @Test
  void getAllLessonsForUser() {
    // Given
    List<LessonDto> lessons = Arrays.asList(lessonDto, lessonDto);
    Mockito.when(lessonService.getAllLessonsForUser(1L)).thenReturn(lessons);

    // When
    ResponseEntity<List<LessonDto>> response = lessonController.getAllLessonsForUser(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
  }

  @Test
  void updateLesson() {
    // Given
    Mockito.when(lessonService.updateLesson(any(LessonDto.class))).thenReturn(lessonDto);

    // When
    ResponseEntity<LessonDto> response = lessonController.updateLesson(lessonDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(lessonDto.getLessonId(), response.getBody().getLessonId());
  }

  @Test
  void getAllLessonsEmpty() {
    // Given
    Mockito.when(lessonService.getAllLessons()).thenReturn(Collections.emptyList());

    // When
    ResponseEntity<List<LessonDto>> response = lessonController.getAllLessons();

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, response.getBody().size());
  }
}
