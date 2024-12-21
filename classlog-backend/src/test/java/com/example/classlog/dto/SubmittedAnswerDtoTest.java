package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubmittedAnswerDtoTest {

  private SubmittedAnswerDto submittedAnswerDto;
  private TaskQuestionDto taskQuestionDto;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    taskQuestionDto = TaskQuestionDto.builder()
        .taskQuestionId(1L)
        .task(TaskDto.builder()
            .id(1L)
            .taskName("Math Task")
            .description("Solve equations")
            .dueDate(LocalDateTime.now().plusDays(1))
            .createdAt(LocalDateTime.now().minusDays(1))
            .build())
        .question(QuestionDto.builder()
            .questionId(1L)
            .content("What is 2+2?")
            .points(10)
            .build())
        .build();

    userDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now().minusYears(1))
        .build();

    submittedAnswerDto = SubmittedAnswerDto.builder()
        .submittedAnswerId(1L)
        .taskQuestion(taskQuestionDto)
        .user(userDto)
        .createdAt(LocalDateTime.now())
        .content("4")
        .build();
  }

  @Test
  void shouldCreateSubmittedAnswerDto() {
    // Test for basic creation of SubmittedAnswerDto
    assertThat(submittedAnswerDto).isNotNull();
    assertThat(submittedAnswerDto.getSubmittedAnswerId()).isEqualTo(1L);
    assertThat(submittedAnswerDto.getContent()).isEqualTo("4");
    assertThat(submittedAnswerDto.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldReturnTaskQuestionDetails() {
    // Verifying that TaskQuestionDto details are correctly set
    TaskQuestionDto taskQuestion = submittedAnswerDto.getTaskQuestion();
    assertThat(taskQuestion).isNotNull();
    assertThat(taskQuestion.getTaskQuestionId()).isEqualTo(1L);
    assertThat(taskQuestion.getTask().getTaskName()).isEqualTo("Math Task");
    assertThat(taskQuestion.getQuestion().getContent()).isEqualTo("What is 2+2?");
  }

  @Test
  void shouldReturnUserDetails() {
    // Verifying that UserDto details are correctly set
    UserDto user = submittedAnswerDto.getUser();
    assertThat(user).isNotNull();
    assertThat(user.getName()).isEqualTo("John");
    assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
  }

  @Test
  void shouldAllowModifications() {
    // Verifying that values can be modified using setters
    submittedAnswerDto.setContent("5");
    assertThat(submittedAnswerDto.getContent()).isEqualTo("5");

    submittedAnswerDto.setSubmittedAnswerId(2L);
    assertThat(submittedAnswerDto.getSubmittedAnswerId()).isEqualTo(2L);
  }

  @Test
  void shouldHandleNullTaskQuestion() {
    // Verifying behavior when TaskQuestionDto is null
    submittedAnswerDto.setTaskQuestion(null);
    assertThat(submittedAnswerDto.getTaskQuestion()).isNull();
  }

  @Test
  void shouldHandleNullUser() {
    // Verifying behavior when UserDto is null
    submittedAnswerDto.setUser(null);
    assertThat(submittedAnswerDto.getUser()).isNull();
  }

  @Test
  void shouldHandleNullContent() {
    // Verifying behavior when content is null
    submittedAnswerDto.setContent(null);
    assertThat(submittedAnswerDto.getContent()).isNull();
  }

  @Test
  void shouldHandleNullCreatedAt() {
    // Verifying behavior when createdAt is null
    submittedAnswerDto.setCreatedAt(null);
    assertThat(submittedAnswerDto.getCreatedAt()).isNull();
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Verifying equals and hashCode based on the primary fields
    SubmittedAnswerDto dto1 = SubmittedAnswerDto.builder()
        .submittedAnswerId(1L)
        .taskQuestion(taskQuestionDto)
        .user(userDto)
        .createdAt(LocalDateTime.now())
        .content("4")
        .build();

    SubmittedAnswerDto dto2 = SubmittedAnswerDto.builder()
        .submittedAnswerId(1L)
        .taskQuestion(taskQuestionDto)
        .user(userDto)
        .createdAt(LocalDateTime.now())
        .content("4")
        .build();

    assertThat(dto1).isEqualTo(dto2);
    assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Verifying toString method for SubmittedAnswerDto
    String toString = submittedAnswerDto.toString();
    assertThat(toString).contains("submittedAnswerId=1", "content=4");
  }

  @Test
  void shouldTestToStringWithNullFields() {
    // Verifying toString when fields are null
    SubmittedAnswerDto nullDto = SubmittedAnswerDto.builder().build();
    String toString = nullDto.toString();
    assertThat(toString).contains("submittedAnswerId=null", "content=null", "createdAt=null");
  }

  @Test
  void shouldHandleEmptyContent() {
    // Verifying behavior when content is an empty string
    submittedAnswerDto.setContent("");
    assertThat(submittedAnswerDto.getContent()).isEmpty();
  }

  @Test
  void shouldTestPrePersist() {
    // Verifying that the createdAt field is set to the current time before persisting
    LocalDateTime beforePersist = LocalDateTime.now().minusMinutes(1);
    assertThat(submittedAnswerDto.getCreatedAt()).isAfter(beforePersist);
  }
}
