package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubmittedTaskDtoTest {

  private SubmittedTaskDto submittedTaskDto;
  private TaskDto taskDto;
  private UserDto userDto;
  private QuestionWithUserAnswerDto questionWithUserAnswerDto;

  @BeforeEach
  void setUp() {
    taskDto = TaskDto.builder()
        .id(1L)
        .taskName("Math Homework")
        .description("Complete algebra problems")
        .dueDate(LocalDateTime.now().plusDays(1))
        .createdAt(LocalDateTime.now().minusDays(1))
        .createdBy(UserDto.builder().id(2L).name("Teacher").surname("Smith").build())
        .score(100)
        .build();

    userDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .role(null)
        .createdAt(LocalDateTime.now().minusYears(1))
        .token("dummyToken")
        .build();

    questionWithUserAnswerDto = QuestionWithUserAnswerDto.builder()
        .question(QuestionDto.builder()
            .questionId(1L)
            .content("What is 2+2?")
            .points(10)
            .build())
        .answers(List.of(
            AnswerDto.builder().id(1L).content("4").isCorrect(true).build(),
            AnswerDto.builder().id(2L).content("5").isCorrect(false).build()
        ))
        .userAnswer("4")
        .score(10)
        .build();

    submittedTaskDto = SubmittedTaskDto.builder()
        .task(taskDto)
        .user(userDto)
        .questionsWithAnswers(List.of(questionWithUserAnswerDto))
        .score(10)
        .build();
  }

  @Test
  void shouldCreateSubmittedTaskDto() {
    assertThat(submittedTaskDto).isNotNull();
    assertThat(submittedTaskDto.getTask()).isEqualTo(taskDto);
    assertThat(submittedTaskDto.getUser()).isEqualTo(userDto);
    assertThat(submittedTaskDto.getQuestionsWithAnswers()).hasSize(1);
    assertThat(submittedTaskDto.getScore()).isEqualTo(10);
  }

  @Test
  void shouldReturnTaskDetails() {
    TaskDto task = submittedTaskDto.getTask();
    assertThat(task).isNotNull();
    assertThat(task.getTaskName()).isEqualTo("Math Homework");
    assertThat(task.getDescription()).isEqualTo("Complete algebra problems");
    assertThat(task.getScore()).isEqualTo(100);
  }

  @Test
  void shouldReturnUserDetails() {
    UserDto user = submittedTaskDto.getUser();
    assertThat(user).isNotNull();
    assertThat(user.getName()).isEqualTo("John");
    assertThat(user.getSurname()).isEqualTo("Doe");
    assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
  }

  @Test
  void shouldReturnQuestionsWithAnswers() {
    List<QuestionWithUserAnswerDto> questions = submittedTaskDto.getQuestionsWithAnswers();
    assertThat(questions).hasSize(1);

    QuestionWithUserAnswerDto questionWithAnswers = questions.get(0);
    assertThat(questionWithAnswers.getQuestion().getContent()).isEqualTo("What is 2+2?");
    assertThat(questionWithAnswers.getAnswers()).hasSize(2);
    assertThat(questionWithAnswers.getUserAnswer()).isEqualTo("4");
    assertThat(questionWithAnswers.getScore()).isEqualTo(10);
  }

  @Test
  void shouldAllowModifications() {
    submittedTaskDto.setScore(20);
    assertThat(submittedTaskDto.getScore()).isEqualTo(20);

    submittedTaskDto.setQuestionsWithAnswers(List.of());
    assertThat(submittedTaskDto.getQuestionsWithAnswers()).isEmpty();
  }

  @Test
  void shouldHandleNullTask() {
    submittedTaskDto.setTask(null);
    assertThat(submittedTaskDto.getTask()).isNull();
  }

  @Test
  void shouldHandleNullUser() {
    submittedTaskDto.setUser(null);
    assertThat(submittedTaskDto.getUser()).isNull();
  }

  @Test
  void shouldHandleNullQuestions() {
    submittedTaskDto.setQuestionsWithAnswers(null);
    assertThat(submittedTaskDto.getQuestionsWithAnswers()).isNull();
  }

  @Test
  void shouldHandleNullScore() {
    submittedTaskDto.setScore(null);
    assertThat(submittedTaskDto.getScore()).isNull();
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    SubmittedTaskDto dto1 = SubmittedTaskDto.builder()
        .task(taskDto)
        .user(userDto)
        .questionsWithAnswers(List.of(questionWithUserAnswerDto))
        .score(10)
        .build();

    SubmittedTaskDto dto2 = SubmittedTaskDto.builder()
        .task(taskDto)
        .user(userDto)
        .questionsWithAnswers(List.of(questionWithUserAnswerDto))
        .score(10)
        .build();

    assertThat(dto1).isEqualTo(dto2);
    assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
  }

  @Test
  void shouldTestToStringWithNullFields() {
    SubmittedTaskDto nullDto = SubmittedTaskDto.builder().build();
    String toString = nullDto.toString();
    assertThat(toString).contains("score=null", "task=null", "user=null",
        "questionsWithAnswers=null");
  }

  @Test
  void shouldHandleEmptyQuestions() {
    submittedTaskDto.setQuestionsWithAnswers(List.of());
    assertThat(submittedTaskDto.getQuestionsWithAnswers()).isEmpty();
  }

  @Test
  void shouldHandleNullScoreWhenModified() {
    submittedTaskDto.setScore(null);
    assertThat(submittedTaskDto.getScore()).isNull();
  }
}
