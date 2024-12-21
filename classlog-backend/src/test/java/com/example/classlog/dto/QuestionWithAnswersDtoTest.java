package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class QuestionWithAnswersDtoTest {

  @Test
  void shouldCreateQuestionWithAnswersDtoUsingBuilder() {
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

    // When
    QuestionWithAnswersDto dto = QuestionWithAnswersDto.builder()
        .question(question)
        .answers(Arrays.asList(answer1, answer2))
        .build();

    // Then
    assertThat(dto.getQuestion()).isEqualTo(question);
    assertThat(dto.getAnswers()).isEqualTo(Arrays.asList(answer1, answer2));
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    QuestionWithAnswersDto dto = QuestionWithAnswersDto.builder().build();

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

    dto.setQuestion(question);
    dto.setAnswers(Arrays.asList(answer1, answer2));

    // Then
    assertThat(dto.getQuestion()).isEqualTo(question);
    assertThat(dto.getAnswers()).isEqualTo(Arrays.asList(answer1, answer2));
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

    QuestionWithAnswersDto dto1 = QuestionWithAnswersDto.builder()
        .question(question1)
        .answers(Arrays.asList(answer1, answer2))
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

    QuestionWithAnswersDto dto2 = QuestionWithAnswersDto.builder()
        .question(question2)
        .answers(Arrays.asList(answer3, answer4))
        .build();

    // When & Then
    assertThat(dto1).isEqualTo(dto2);
    assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    QuestionWithAnswersDto dto = QuestionWithAnswersDto.builder().build();

    // When & Then
    assertThat(dto.getQuestion()).isNull();
    assertThat(dto.getAnswers()).isNull();
  }
}
