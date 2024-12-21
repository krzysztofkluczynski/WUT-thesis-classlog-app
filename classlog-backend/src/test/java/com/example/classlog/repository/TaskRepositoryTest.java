package com.example.classlog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.entities.Question;
import com.example.classlog.entities.SubmittedAnswer;
import com.example.classlog.entities.Task;
import com.example.classlog.entities.TaskQuestion;
import com.example.classlog.entities.User;
import com.example.classlog.entities.UserTask;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TaskRepositoryTest {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserTaskRepository userTaskRepository;

  @Autowired
  private TaskQuestionRepository taskQuestionRepository;

  @Autowired
  private SubmittedAnswerRepository submittedAnswerRepository;

  @Autowired
  private QuestionRepository questionRepository;

  private User creator;
  private User assignee;
  private Task task;
  private Question question;

  @BeforeEach
  void setUp() {
    // Given
    creator = userRepository.save(
        User.builder()
            .name("Creator")
            .surname("User")
            .email("creator@example.com")
            .password("password123")
            .build()
    );

    assignee = userRepository.save(
        User.builder()
            .name("Assignee")
            .surname("User")
            .email("assignee@example.com")
            .password("password123")
            .build()
    );

    task = taskRepository.save(
        Task.builder()
            .taskName("Sample Task")
            .description("Task Description")
            .createdBy(creator)
            .dueDate(LocalDateTime.now().plusDays(1))
            .build()
    );

    userTaskRepository.save(
        UserTask.builder()
            .task(task)
            .user(assignee)
            .build()
    );

    question = questionRepository.save(
        Question.builder()
            .content("Sample Question")
            .points(10)
            .build()
    );
  }

  @Test
  void shouldFindTasksTodoByUser() {
    // When
    List<Task> tasks = taskRepository.findTasksTodoByUser(assignee.getId());

    // Then
    assertThat(tasks).hasSize(1);
    assertThat(tasks.get(0).getTaskName()).isEqualTo("Sample Task");
  }

  @Test
  void shouldFindTasksDoneByUser() {
    // Given
    TaskQuestion taskQuestion = taskQuestionRepository.save(
        TaskQuestion.builder()
            .task(task)
            .question(question)
            .build()
    );

    submittedAnswerRepository.save(
        SubmittedAnswer.builder()
            .taskQuestion(taskQuestion)
            .user(assignee)
            .content("Answer")
            .build()
    );

    // When
    List<Task> tasks = taskRepository.findTasksDoneByUser(assignee.getId());

    // Then
    assertThat(tasks).hasSize(1);
    assertThat(tasks.get(0).getTaskName()).isEqualTo("Sample Task");
  }

  @Test
  void shouldFindOverdueTasksNotSubmittedByUser() {
    // Given
    task.setDueDate(LocalDateTime.now().minusDays(1));
    taskRepository.save(task);

    // When
    List<Task> tasks = taskRepository.findOverdueTasksNotSubmittedByUser(assignee.getId());

    // Then
    assertThat(tasks).hasSize(1);
    assertThat(tasks.get(0).getTaskName()).isEqualTo("Sample Task");
  }

  @Test
  void shouldFindUserTasksWithSubmittedAnswers() {
    // Given
    TaskQuestion taskQuestion = taskQuestionRepository.save(
        TaskQuestion.builder()
            .task(task)
            .question(question)
            .build()
    );

    submittedAnswerRepository.save(
        SubmittedAnswer.builder()
            .taskQuestion(taskQuestion)
            .user(assignee)
            .content("Answer")
            .build()
    );

    // When
    List<UserTask> userTasks = taskRepository.findUserTasksWithSubmittedAnswers(creator.getId());

    // Then
    assertThat(userTasks).hasSize(1);
    assertThat(userTasks.get(0).getTask().getTaskName()).isEqualTo("Sample Task");
  }

  @Test
  void shouldFindOverdueUserTasksWithoutSubmittedAnswers() {
    // Given
    task.setDueDate(LocalDateTime.now().minusDays(1));
    taskRepository.save(task);

    // When
    List<UserTask> userTasks = taskRepository.findOverdueUserTasksWithoutSubmittedAnswers(
        creator.getId());

    // Then
    assertThat(userTasks).hasSize(1);
    assertThat(userTasks.get(0).getTask().getTaskName()).isEqualTo("Sample Task");
  }
}
