package com.example.classlog.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskTest {

  private User createdBy;
  private Task task;

  @BeforeEach
  void setUp() {
    // Setting up test data for User entity
    createdBy = User.builder()
        .id(1L)
        .name("Alice")
        .surname("Smith")
        .email("alice.smith@example.com")
        .build();

    task = Task.builder()
        .id(1L)
        .createdBy(createdBy)
        .taskName("Complete Assignment")
        .description("Finish the math assignment")
        .dueDate(LocalDateTime.of(2024, 12, 31, 23, 59))
        .score(95)
        .createdAt(LocalDateTime.MIN)
        .build();
  }

  @Test
  void shouldCreateTaskUsingConstructor() {
    // Given
    Long taskId = 1L;
    String taskName = "Complete Assignment";
    String description = "Finish the math assignment";

    // When
    Task task = new Task(taskId, createdBy, taskName, description,
        LocalDateTime.of(2024, 12, 31, 23, 59), LocalDateTime.MIN, 95);

    // Then
    assertThat(task.getId()).isEqualTo(taskId);
    assertThat(task.getCreatedBy()).isEqualTo(createdBy);
    assertThat(task.getTaskName()).isEqualTo(taskName);
    assertThat(task.getDescription()).isEqualTo(description);
    assertThat(task.getDueDate()).isEqualTo(LocalDateTime.of(2024, 12, 31, 23, 59));
    assertThat(task.getCreatedAt()).isEqualTo(LocalDateTime.MIN);
    assertThat(task.getScore()).isEqualTo(95);
  }

  @Test
  void shouldCreateTaskUsingBuilder() {
    // Given
    Long taskId = 1L;
    String taskName = "Complete Assignment";
    String description = "Finish the math assignment";

    // When
    Task task = Task.builder()
        .id(taskId)
        .createdBy(createdBy)
        .taskName(taskName)
        .description(description)
        .dueDate(LocalDateTime.of(2024, 12, 31, 23, 59))
        .createdAt(LocalDateTime.MIN)
        .score(95)
        .build();

    // Then
    assertThat(task.getId()).isEqualTo(taskId);
    assertThat(task.getCreatedBy()).isEqualTo(createdBy);
    assertThat(task.getTaskName()).isEqualTo(taskName);
    assertThat(task.getDescription()).isEqualTo(description);
    assertThat(task.getDueDate()).isEqualTo(LocalDateTime.of(2024, 12, 31, 23, 59));
    assertThat(task.getCreatedAt()).isEqualTo(LocalDateTime.MIN);
    assertThat(task.getScore()).isEqualTo(95);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    Task task = new Task();

    // When
    task.setId(1L);
    task.setCreatedBy(createdBy);
    task.setTaskName("Complete Assignment");
    task.setDescription("Finish the math assignment");
    task.setDueDate(LocalDateTime.of(2024, 12, 31, 23, 59));
    task.setCreatedAt(LocalDateTime.MIN);
    task.setScore(95);

    // Then
    assertThat(task.getId()).isEqualTo(1L);
    assertThat(task.getCreatedBy()).isEqualTo(createdBy);
    assertThat(task.getTaskName()).isEqualTo("Complete Assignment");
    assertThat(task.getDescription()).isEqualTo("Finish the math assignment");
    assertThat(task.getDueDate()).isEqualTo(LocalDateTime.of(2024, 12, 31, 23, 59));
    assertThat(task.getCreatedAt()).isEqualTo(LocalDateTime.MIN);
    assertThat(task.getScore()).isEqualTo(95);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    Task task1 = Task.builder()
        .id(1L)
        .createdBy(createdBy)
        .taskName("Complete Assignment")
        .description("Finish the math assignment")
        .dueDate(LocalDateTime.of(2024, 12, 31, 23, 59))
        .createdAt(LocalDateTime.MIN)
        .score(95)
        .build();

    Task task2 = Task.builder()
        .id(1L)
        .createdBy(createdBy)
        .taskName("Complete Assignment")
        .description("Finish the math assignment")
        .dueDate(LocalDateTime.of(2024, 12, 31, 23, 59))
        .createdAt(LocalDateTime.MIN)
        .score(95)
        .build();

    // When & Then
    assertThat(task1).isEqualTo(task2);
    assertThat(task1.hashCode()).isEqualTo(task2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    Task task = Task.builder()
        .id(1L)
        .createdBy(createdBy)
        .taskName("Complete Assignment")
        .description("Finish the math assignment")
        .dueDate(LocalDateTime.of(2024, 12, 31, 23, 59))
        .score(95)
        .createdAt(LocalDateTime.MIN)
        .build();

    // When & Then
    String toString = task.toString();
    assertThat(toString).contains("id=1", "taskName=Complete Assignment",
        "description=Finish the math assignment");
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    Task task = new Task();

    // When & Then
    assertThat(task.getId()).isNull();
    assertThat(task.getCreatedBy()).isNull();
    assertThat(task.getTaskName()).isNull();
    assertThat(task.getDescription()).isNull();
    assertThat(task.getDueDate()).isNull();
    assertThat(task.getCreatedAt()).isNull();
    assertThat(task.getScore()).isNull();
  }

  @Test
  void shouldHandleNullValuesForBuilder() {
    // Given
    Task task = Task.builder().build();

    // When & Then
    assertThat(task.getId()).isNull();
    assertThat(task.getCreatedBy()).isNull();
    assertThat(task.getTaskName()).isNull();
    assertThat(task.getDescription()).isNull();
    assertThat(task.getDueDate()).isNull();
    assertThat(task.getCreatedAt()).isNull();
    assertThat(task.getScore()).isNull();
  }

  @Test
  void shouldTestPrePersist() {
    // Given
    Task task = new Task();
    task.setCreatedBy(createdBy);
    task.setTaskName("Complete Assignment");
    task.setDescription("Finish the math assignment");

    // When: Trigger the persistence lifecycle event
    task.onCreate(); // This will set the createdAt field

    // Then
    assertThat(task.getCreatedAt()).isNotNull();
    assertThat(task.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
  }
}
