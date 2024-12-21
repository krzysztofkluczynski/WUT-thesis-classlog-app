package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.entities.QuestionType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class QuestionDtoTest {

  @Test
  void shouldCreateQuestionDtoUsingBuilder() {
    // Given
    QuestionType questionType = new QuestionType(); // Adjust as needed for a valid object

    // When
    QuestionDto questionDto = QuestionDto.builder()
        .questionId(1L)
        .questionType(questionType)
        .editedAt(LocalDateTime.MIN)
        .points(10)
        .content("What is Java?")
        .file(null)  // Or provide a valid FileDto object
        .build();

    // Then
    assertThat(questionDto.getQuestionId()).isEqualTo(1L);
    assertThat(questionDto.getQuestionType()).isEqualTo(questionType);
    assertThat(questionDto.getEditedAt()).isNotNull();
    assertThat(questionDto.getPoints()).isEqualTo(10);
    assertThat(questionDto.getContent()).isEqualTo("What is Java?");
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    QuestionDto questionDto = new QuestionDto();

    // When
    questionDto.setQuestionId(1L);
    questionDto.setQuestionType(null); // Or a valid QuestionType object
    questionDto.setEditedAt(LocalDateTime.MIN);
    questionDto.setPoints(5);
    questionDto.setContent("Test question");
    questionDto.setFile(null);  // Or a valid FileDto object

    // Then
    assertThat(questionDto.getQuestionId()).isEqualTo(1L);
    assertThat(questionDto.getQuestionType()).isNull();
    assertThat(questionDto.getEditedAt()).isNotNull();
    assertThat(questionDto.getPoints()).isEqualTo(5);
    assertThat(questionDto.getContent()).isEqualTo("Test question");
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    QuestionType questionType = new QuestionType(); // Adjust as needed

    QuestionDto questionDto1 = QuestionDto.builder()
        .questionId(1L)
        .questionType(questionType)
        .editedAt(LocalDateTime.MIN)
        .points(10)
        .content("What is Java?")
        .file(null)
        .build();

    QuestionDto questionDto2 = QuestionDto.builder()
        .questionId(1L)
        .questionType(questionType)
        .editedAt(LocalDateTime.MIN)
        .points(10)
        .content("What is Java?")
        .file(null)
        .build();

    // When & Then
    assertThat(questionDto1).isEqualTo(questionDto2);
    assertThat(questionDto1.hashCode()).isEqualTo(questionDto2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    QuestionType questionType = new QuestionType(); // Adjust as needed
    QuestionDto questionDto = QuestionDto.builder()
        .questionId(1L)
        .questionType(questionType)
        .editedAt(LocalDateTime.MIN)
        .points(10)
        .content("What is Java?")
        .file(null)
        .build();

    // When & Then
    assertThat(questionDto.toString()).contains("questionId=1", "content=What is Java?");
  }
}
