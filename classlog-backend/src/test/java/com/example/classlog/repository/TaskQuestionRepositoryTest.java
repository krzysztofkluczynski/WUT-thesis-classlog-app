package com.example.classlog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.entities.Question;
import com.example.classlog.entities.Task;
import com.example.classlog.entities.TaskQuestion;
import com.example.classlog.entities.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TaskQuestionRepositoryTest {

  @Autowired
  private TaskQuestionRepository taskQuestionRepository;

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private UserRepository userRepository;

  private Task task;
  private Question question;

  @BeforeEach
  void setUp() {
    // Given
    User user = userRepository.save(
        User.builder()
            .name("John")
            .surname("Doe")
            .email("john.doe@example.com")
            .password("password123")
            .build()
    );

    task = taskRepository.save(
        Task.builder()
            .taskName("Sample Task")
            .description("Task Description")
            .createdBy(user)
            .build()
    );

    question = questionRepository.save(
        Question.builder()
            .content("What is Java?")
            .points(10)
            .build()
    );

    taskQuestionRepository.save(
        TaskQuestion.builder()
            .task(task)
            .question(question)
            .build()
    );
  }

  @Test
  void shouldFindAllTaskQuestionsByTaskId() {
    // Given
    Long taskId = task.getId();

    // When
    List<TaskQuestion> taskQuestions = taskQuestionRepository.findAllByTaskId(taskId);

    // Then
    assertThat(taskQuestions).hasSize(1);
    assertThat(taskQuestions.get(0).getQuestion().getContent()).isEqualTo("What is Java?");
  }

  @Test
  void shouldFindTaskQuestionByQuestionIdAndTaskId() {
    // Given
    Long taskId = task.getId();
    Long questionId = question.getQuestionId();

    // When
    Optional<TaskQuestion> taskQuestion = taskQuestionRepository.findTaskQuestionByQuestionIdAndTaskId(
        questionId, taskId);

    // Then
    assertThat(taskQuestion).isPresent();
    assertThat(taskQuestion.get().getTask().getTaskName()).isEqualTo("Sample Task");
    assertThat(taskQuestion.get().getQuestion().getContent()).isEqualTo("What is Java?");
    assertThat(taskQuestion.get().getQuestion().getPoints()).isEqualTo(10);
  }
}
