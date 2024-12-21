package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTaskDtoTest {

  private UserTaskDto userTaskDto;
  private UserDto userDto;
  private TaskDto taskDto;

  @BeforeEach
  void setUp() {
    userDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now().minusYears(1))
        .build();

    taskDto = TaskDto.builder()
        .id(1L)
        .taskName("Math Task")
        .description("Solve equations")
        .dueDate(LocalDateTime.now().plusDays(1))
        .createdAt(LocalDateTime.now().minusDays(1))
        .score(100)
        .build();

    userTaskDto = UserTaskDto.builder()
        .userTaskId(1L)
        .user(userDto)
        .task(taskDto)
        .score(90)
        .build();
  }

  @Test
  void shouldCreateUserTaskDto() {
    assertThat(userTaskDto).isNotNull();
    assertThat(userTaskDto.getUserTaskId()).isEqualTo(1L);
    assertThat(userTaskDto.getScore()).isEqualTo(90);
    assertThat(userTaskDto.getUser()).isEqualTo(userDto);
    assertThat(userTaskDto.getTask()).isEqualTo(taskDto);
  }

  @Test
  void shouldReturnUserDetails() {
    UserDto user = userTaskDto.getUser();
    assertThat(user).isNotNull();
    assertThat(user.getId()).isEqualTo(1L);
    assertThat(user.getName()).isEqualTo("John");
    assertThat(user.getSurname()).isEqualTo("Doe");
    assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
    assertThat(user.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldReturnTaskDetails() {
    TaskDto task = userTaskDto.getTask();
    assertThat(task).isNotNull();
    assertThat(task.getId()).isEqualTo(1L);
    assertThat(task.getTaskName()).isEqualTo("Math Task");
    assertThat(task.getDescription()).isEqualTo("Solve equations");
    assertThat(task.getDueDate()).isNotNull();
    assertThat(task.getCreatedAt()).isNotNull();
    assertThat(task.getScore()).isEqualTo(100);
  }

  @Test
  void shouldAllowModifications() {
    userTaskDto.setScore(95);
    assertThat(userTaskDto.getScore()).isEqualTo(95);

    userTaskDto.setUserTaskId(2L);
    assertThat(userTaskDto.getUserTaskId()).isEqualTo(2L);

    UserDto newUser = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .createdAt(LocalDateTime.now().minusMonths(6))
        .build();
    userTaskDto.setUser(newUser);
    assertThat(userTaskDto.getUser()).isEqualTo(newUser);

    TaskDto newTask = TaskDto.builder()
        .id(2L)
        .taskName("Science Task")
        .description("Complete experiments")
        .dueDate(LocalDateTime.now().plusDays(2))
        .createdAt(LocalDateTime.now().minusDays(2))
        .score(80)
        .build();
    userTaskDto.setTask(newTask);
    assertThat(userTaskDto.getTask()).isEqualTo(newTask);
  }

  @Test
  void shouldHandleNullValues() {
    userTaskDto.setUser(null);
    userTaskDto.setTask(null);
    userTaskDto.setScore(null);

    assertThat(userTaskDto.getUser()).isNull();
    assertThat(userTaskDto.getTask()).isNull();
    assertThat(userTaskDto.getScore()).isNull();
  }
}
