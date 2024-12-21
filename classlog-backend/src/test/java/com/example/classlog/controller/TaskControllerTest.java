package com.example.classlog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.example.classlog.dto.SubmittedTaskDto;
import com.example.classlog.dto.TaskDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.dto.UserTaskDto;
import com.example.classlog.service.TaskService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class TaskControllerTest {

  @Mock
  private TaskService taskService;

  @InjectMocks
  private TaskController taskController;

  private TaskDto taskDto;
  private SubmittedTaskDto submittedTaskDto;
  private UserTaskDto userTaskDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    UserDto userDto = UserDto.builder()
        .id(1L)
        .email("user@example.com")
        .build();

    taskDto = TaskDto.builder()
        .id(1L)
        .taskName("Sample Task")
        .description("This is a sample task description.")
        .dueDate(LocalDateTime.now().plusDays(1))
        .createdAt(LocalDateTime.now())
        .createdBy(userDto)
        .score(100)
        .build();

    submittedTaskDto = SubmittedTaskDto.builder()
        .task(taskDto)
        .user(userDto)
        .score(90)
        .questionsWithAnswers(Collections.emptyList())
        .build();

    userTaskDto = UserTaskDto.builder()
        .userTaskId(1L)
        .user(userDto)
        .task(taskDto)
        .score(85)
        .build();
  }

  @Test
  void getAllTasks() {
    // Given
    List<TaskDto> tasks = Arrays.asList(taskDto, taskDto);
    Mockito.when(taskService.getAllTasks()).thenReturn(tasks);

    // When
    ResponseEntity<List<TaskDto>> response = taskController.getAllTasks();

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
  }

  @Test
  void getTaskById() {
    // Given
    Mockito.when(taskService.getTaskById(1L)).thenReturn(Optional.of(taskDto));

    // When
    ResponseEntity<TaskDto> response = taskController.getTaskById(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(taskDto.getId(), response.getBody().getId());
  }

  @Test
  void createTask() {
    // Given
    Mockito.when(taskService.createTask(any(TaskDto.class))).thenReturn(taskDto);

    // When
    ResponseEntity<TaskDto> response = taskController.createTask(taskDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(taskDto.getId(), response.getBody().getId());
  }

  @Test
  void deleteTask() {
    // Given
    Mockito.when(taskService.deleteTask(1L)).thenReturn(true);

    // When
    ResponseEntity<Void> response = taskController.deleteTask(1L);

    // Then
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  void assignUsersToTask() {
    // Given
    Mockito.when(taskService.assignUsersToTask(eq(1L), any(List.class))).thenReturn(true);

    // When
    ResponseEntity<Void> response = taskController.assignUsersToTask(1L, Collections.emptyList());

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void submitTask() {
    // Given
    Mockito.when(taskService.processSubmittedTask(any(SubmittedTaskDto.class))).thenReturn(true);

    // When
    ResponseEntity<Void> response = taskController.submitTask(submittedTaskDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void getSubmittedTaskDetails() {
    // Given
    Mockito.when(taskService.getSubmittedTaskDetails(1L, 1L))
        .thenReturn(Optional.of(submittedTaskDto));

    // When
    ResponseEntity<SubmittedTaskDto> response = taskController.getSubmittedTaskDetails(1L, 1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(submittedTaskDto.getTask().getId(), response.getBody().getTask().getId());
  }

  @Test
  void updateScore() {
    // Given
    Mockito.when(taskService.updateUserTaskScore(eq(1L), eq(1L), eq(95))).thenReturn(true);

    // When
    ResponseEntity<?> response = taskController.updateScore(1L, 1L, Map.of("newScore", 95));

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void getAllTasksEmpty() {
    // Given
    Mockito.when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

    // When
    ResponseEntity<List<TaskDto>> response = taskController.getAllTasks();

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, response.getBody().size());
  }
}
