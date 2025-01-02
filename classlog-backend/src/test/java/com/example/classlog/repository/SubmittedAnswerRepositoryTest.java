package com.example.classlog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.entity.Question;
import com.example.classlog.entity.SubmittedAnswer;
import com.example.classlog.entity.Task;
import com.example.classlog.entity.TaskQuestion;
import com.example.classlog.entity.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SubmittedAnswerRepositoryTest {

  @Autowired
  private SubmittedAnswerRepository submittedAnswerRepository;

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private TaskQuestionRepository taskQuestionRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private QuestionRepository questionRepository;

  private Task task;
  private TaskQuestion taskQuestion;
  private User user;
  private Question question;

  @BeforeEach
  void setUp() {
    // Given
    user = userRepository.save(
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
            .content("Sample Question")
            .points(10)
            .build()
    );

    taskQuestion = taskQuestionRepository.save(
        TaskQuestion.builder()
            .task(task)
            .question(question)
            .build()
    );

    submittedAnswerRepository.save(
        SubmittedAnswer.builder()
            .taskQuestion(taskQuestion)
            .user(user)
            .content("Sample Answer")
            .build()
    );
  }

  @Test
  void shouldFindSubmittedAnswersByTaskIdAndUserId() {
    // Given
    Long taskId = task.getId();
    Long userId = user.getId();

    // When
    List<SubmittedAnswer> submittedAnswers = submittedAnswerRepository.findByTaskQuestion_Task_IdAndUser_Id(
        taskId, userId);

    // Then
    assertThat(submittedAnswers).hasSize(1);
    assertThat(submittedAnswers.get(0).getContent()).isEqualTo("Sample Answer");
    assertThat(submittedAnswers.get(0).getCreatedAt()).isNotNull();
  }
}
