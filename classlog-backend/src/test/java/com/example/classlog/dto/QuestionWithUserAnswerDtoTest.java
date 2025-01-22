package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class QuestionWithUserAnswerDtoTest {

  @Test
  void shouldCreateQuestionWithAnswersAndUserAnswerDtoUsingBuilder() {
    // Given
    QuestionDto question = QuestionDto.builder()
        .questionId(1L)
        .content("What is 2 + 2?")
        .points(5)
        .build();
    AnswerDto answer1 = AnswerDto.builder()
        .id(1L)
        .content("4")
        .isCorrect(true)
        .build();
    AnswerDto answer2 = AnswerDto.builder()
        .id(2L)
        .content("5")
        .isCorrect(false)
        .build();
    String userAnswer = "4";
    Integer score = 5;

    // When
    QuestionWithUserAnswerDto dto = QuestionWithUserAnswerDto.builder()
        .question(question)
        .answers(Arrays.asList(answer1, answer2))
        .userAnswer(userAnswer)
        .score(score)
        .build();

    // Then
    assertThat(dto.getQuestion()).isEqualTo(question);
    assertThat(dto.getAnswers()).isEqualTo(Arrays.asList(answer1, answer2));
    assertThat(dto.getUserAnswer()).isEqualTo(userAnswer);
    assertThat(dto.getScore()).isEqualTo(score);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    QuestionWithUserAnswerDto dto = QuestionWithUserAnswerDto.builder().build();

    // When
    QuestionDto question = QuestionDto.builder()
        .questionId(1L)
        .content("What is 2 + 2?")
        .points(5)
        .build();
    AnswerDto answer1 = AnswerDto.builder()
        .id(1L)
        .content("4")
        .isCorrect(true)
        .build();
    AnswerDto answer2 = AnswerDto.builder()
        .id(2L)
        .content("5")
        .isCorrect(false)
        .build();
    String userAnswer = "4";
    Integer score = 5;

    dto.setQuestion(question);
    dto.setAnswers(Arrays.asList(answer1, answer2));
    dto.setUserAnswer(userAnswer);
    dto.setScore(score);

    // Then
    assertThat(dto.getQuestion()).isEqualTo(question);
    assertThat(dto.getAnswers()).isEqualTo(Arrays.asList(answer1, answer2));
    assertThat(dto.getUserAnswer()).isEqualTo(userAnswer);
    assertThat(dto.getScore()).isEqualTo(score);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    QuestionDto question1 = QuestionDto.builder()
        .questionId(1L)
        .content("What is 2 + 2?")
        .points(5)
        .build();
    AnswerDto answer1 = AnswerDto.builder()
        .id(1L)
        .content("4")
        .isCorrect(true)
        .build();
    AnswerDto answer2 = AnswerDto.builder()
        .id(2L)
        .content("5")
        .isCorrect(false)
        .build();
    String userAnswer1 = "4";
    Integer score1 = 5;

    QuestionWithUserAnswerDto dto1 = QuestionWithUserAnswerDto.builder()
        .question(question1)
        .answers(Arrays.asList(answer1, answer2))
        .userAnswer(userAnswer1)
        .score(score1)
        .build();

    QuestionDto question2 = QuestionDto.builder()
        .questionId(1L)
        .content("What is 2 + 2?")
        .points(5)
        .build();
    AnswerDto answer3 = AnswerDto.builder()
        .id(1L)
        .content("4")
        .isCorrect(true)
        .build();
    AnswerDto answer4 = AnswerDto.builder()
        .id(2L)
        .content("5")
        .isCorrect(false)
        .build();
    String userAnswer2 = "4";
    Integer score2 = 5;

    QuestionWithUserAnswerDto dto2 = QuestionWithUserAnswerDto.builder()
        .question(question2)
        .answers(Arrays.asList(answer3, answer4))
        .userAnswer(userAnswer2)
        .score(score2)
        .build();

    // When & Then
    assertThat(dto1).isEqualTo(dto2);
    assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    QuestionDto question = QuestionDto.builder()
        .questionId(1L)
        .content("What is 2 + 2?")
        .points(5)
        .build();
    AnswerDto answer1 = AnswerDto.builder()
        .id(1L)
        .content("4")
        .isCorrect(true)
        .build();
    AnswerDto answer2 = AnswerDto.builder()
        .id(2L)
        .content("5")
        .isCorrect(false)
        .build();
    String userAnswer = "4";
    Integer score = 5;

    QuestionWithUserAnswerDto dto = QuestionWithUserAnswerDto.builder()
        .question(question)
        .answers(Arrays.asList(answer1, answer2))
        .userAnswer(userAnswer)
        .score(score)
        .build();

    // When & Then
    String toString = dto.toString();

    // Simplified toString check, omitting the full AnswerDto and QuestionDto details
    assertThat(toString).contains(
        "score=5",
        "userAnswer=4"
    );
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    QuestionWithUserAnswerDto dto = QuestionWithUserAnswerDto.builder().build();

    // When & Then
    assertThat(dto.getQuestion()).isNull();
    assertThat(dto.getAnswers()).isNull();
    assertThat(dto.getUserAnswer()).isNull();
    assertThat(dto.getScore()).isNull();
  }
}
