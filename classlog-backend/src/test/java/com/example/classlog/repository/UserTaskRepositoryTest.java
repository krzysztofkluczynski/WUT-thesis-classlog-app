package com.example.classlog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.entity.Task;
import com.example.classlog.entity.User;
import com.example.classlog.entity.UserTask;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserTaskRepositoryTest {

  @Autowired
  private UserTaskRepository userTaskRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TaskRepository taskRepository;

  private User creator;
  private User assignee;
  private Task task;

  @BeforeEach
  void setUp() {
    // Given
    creator = userRepository.save(User.builder()
        .name("Creator")
        .surname("User")
        .email("creator@example.com")
        .password("password")
        .build());

    assignee = userRepository.save(User.builder()
        .name("Assignee")
        .surname("User")
        .email("assignee@example.com")
        .password("password")
        .build());

    task = taskRepository.save(Task.builder()
        .taskName("Sample Task")
        .description("Task Description")
        .createdBy(creator)
        .dueDate(LocalDateTime.now().plusDays(1))
        .build());

    userTaskRepository.save(UserTask.builder()
        .task(task)
        .user(assignee)
        .build());
  }

  @Test
  void shouldFindTasksCreatedByUserWithSubmittedAnswers() {
    // When
    List<UserTask> tasks = userTaskRepository.findTasksCreatedByUserWithSubmittedAnswers(
        creator.getId());

    // Then
    assertThat(tasks).isEmpty(); // Adjust based on data setup
  }

  @Test
  void shouldFindOverdueTasksNotSubmittedByCreatedByUser() {
    // Given
    task.setDueDate(LocalDateTime.now().minusDays(1));
    taskRepository.save(task);

    // When
    List<UserTask> tasks = userTaskRepository.findOverdueTasksNotSubmittedByCreatedByUser(
        creator.getId());

    // Then
    assertThat(tasks).hasSize(1);
    assertThat(tasks.get(0).getTask().getTaskName()).isEqualTo("Sample Task");
  }
}
