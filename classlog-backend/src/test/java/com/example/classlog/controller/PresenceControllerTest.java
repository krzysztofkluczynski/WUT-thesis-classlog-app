package com.example.classlog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.classlog.dto.LessonPresenceDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.service.PresenceService;
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


class PresenceControllerTest {

  @Mock
  private PresenceService presenceService;

  @InjectMocks
  private PresenceController presenceController;

  private LessonPresenceDto lessonPresenceDto;
  private UserDto user1;
  private UserDto user2;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    user1 = UserDto.builder()
        .id(1L)
        .email("user1@example.com")
        .build();

    user2 = UserDto.builder()
        .id(2L)
        .email("user2@example.com")
        .build();

    lessonPresenceDto = LessonPresenceDto.builder()
        .lessonId(1L)
        .users(Arrays.asList(user1, user2))
        .build();
  }

  @Test
  void addUsersToClass() {
    // When
    ResponseEntity<String> response = presenceController.addUsersToClass(lessonPresenceDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Presence submitted.", response.getBody());
  }

  @Test
  void getPresentStudents() {
    // Given
    List<UserDto> students = Arrays.asList(user1, user2);
    Mockito.when(presenceService.getPresentStudents(1L)).thenReturn(students);

    // When
    ResponseEntity<List<UserDto>> response = presenceController.getPresentStudents(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, Objects.requireNonNull(response.getBody()).size());
  }

  @Test
  void updateLessonPresence() {
    // Given
    List<Long> presentStudentIds = Arrays.asList(1L, 2L);

    // When
    ResponseEntity<String> response = presenceController.updateLessonPresence(1L,
        presentStudentIds);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Presence updated.", response.getBody());
  }

  @Test
  void getPresentStudentsEmpty() {
    // Given
    Mockito.when(presenceService.getPresentStudents(1L)).thenReturn(Collections.emptyList());

    // When
    ResponseEntity<List<UserDto>> response = presenceController.getPresentStudents(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, Objects.requireNonNull(response.getBody()).size());
  }
}
