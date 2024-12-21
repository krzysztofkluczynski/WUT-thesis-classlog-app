package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class AnswerDtoTest {

  @Test
  void shouldCreateAnswerDtoUsingBuilder() {
    // Given
    QuestionDto questionDto = QuestionDto.builder()
        .questionId(1L)
        .questionType(null)  // Assuming null for the sake of the test
        .editedAt(LocalDateTime.now())
        .points(5)
        .content("Sample question content")
        .file(null)
        .build();

    // When
    AnswerDto answerDto = AnswerDto.builder()
        .id(1L)
        .question(questionDto)
        .isCorrect(true)
        .content("Sample answer content")
        .build();

    // Then
    assertThat(answerDto.getId()).isEqualTo(1L);
    assertThat(answerDto.getQuestion()).isEqualTo(questionDto);
    assertThat(answerDto.getIsCorrect()).isEqualTo(true);
    assertThat(answerDto.getContent()).isEqualTo("Sample answer content");
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    AnswerDto answerDto = new AnswerDto();

    // When
    answerDto.setId(1L);
    answerDto.setQuestion(null);  // You can also set a valid QuestionDto object here
    answerDto.setIsCorrect(false);
    answerDto.setContent("Answer content");

    // Then
    assertThat(answerDto.getId()).isEqualTo(1L);
    assertThat(answerDto.getQuestion()).isNull();  // or check with a valid QuestionDto object
    assertThat(answerDto.getIsCorrect()).isEqualTo(false);
    assertThat(answerDto.getContent()).isEqualTo("Answer content");
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    QuestionDto questionDto1 = QuestionDto.builder()
        .questionId(1L)
        .questionType(null)
        .editedAt(LocalDateTime.now())
        .points(5)
        .content("Sample question content")
        .file(null)
        .build();

    AnswerDto answerDto1 = AnswerDto.builder()
        .id(1L)
        .question(questionDto1)
        .isCorrect(true)
        .content("Answer content")
        .build();

    AnswerDto answerDto2 = AnswerDto.builder()
        .id(1L)
        .question(questionDto1)
        .isCorrect(true)
        .content("Answer content")
        .build();

    // When & Then
    assertThat(answerDto1).isEqualTo(answerDto2);
    assertThat(answerDto1.hashCode()).isEqualTo(answerDto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    QuestionDto questionDto = QuestionDto.builder()
        .questionId(1L)
        .questionType(null)
        .editedAt(LocalDateTime.now())
        .points(5)
        .content("Sample question content")
        .file(null)
        .build();

    AnswerDto answerDto = AnswerDto.builder()
        .id(1L)
        .question(questionDto)
        .isCorrect(true)
        .content("Answer content")
        .build();

    // When & Then
    assertThat(answerDto.toString()).contains("id=1",
        "isCorrect=true", "content=Answer content");
  }
}
