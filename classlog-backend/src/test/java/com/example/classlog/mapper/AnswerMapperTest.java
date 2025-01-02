package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.AnswerDto;
import com.example.classlog.dto.QuestionDto;
import com.example.classlog.entity.Answer;
import com.example.classlog.entity.Question;
import com.example.classlog.entity.QuestionType;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AnswerMapperTest {

  private AnswerMapperImpl answerMapper;

  @Mock
  private QuestionMapper questionMapper;

  private Answer answer;
  private AnswerDto answerDto;
  private Question question;
  private QuestionDto questionDto;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // Manually instantiate the AnswerMapper and inject QuestionMapper
    answerMapper = new AnswerMapperImpl();

    Field questionMapperField = AnswerMapperImpl.class.getDeclaredField("questionMapper");
    questionMapperField.setAccessible(true);
    questionMapperField.set(answerMapper, questionMapper);

    // Initialize objects
    question = Question.builder()
        .questionId(1L)
        .questionType(QuestionType.builder()
            .questionTypeId(1L)
            .typeName("Multiple Choice")
            .build())
        .content("What is Java?")
        .points(5)
        .editedAt(LocalDateTime.now())
        .build();

    questionDto = QuestionDto.builder()
        .questionId(1L)
        .questionType(question.getQuestionType())
        .content("What is Java?")
        .points(5)
        .editedAt(LocalDateTime.now())
        .build();

    answer = Answer.builder()
        .answerId(1L)
        .question(question)
        .isCorrect(true)
        .content("A programming language")
        .build();

    answerDto = AnswerDto.builder()
        .id(1L)
        .question(questionDto)
        .isCorrect(true)
        .content("A programming language")
        .build();
  }

  @Test
  void shouldMapAnswerToAnswerDto() {
    // Given
    when(questionMapper.toQuestionDto(question)).thenReturn(questionDto);

    // When
    AnswerDto mappedDto = answerMapper.toAnswerDto(answer);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getId()).isEqualTo(answer.getAnswerId());
    assertThat(mappedDto.getQuestion()).isEqualTo(questionDto);
    assertThat(mappedDto.getIsCorrect()).isEqualTo(answer.getIsCorrect());
    assertThat(mappedDto.getContent()).isEqualTo(answer.getContent());
  }

  @Test
  void shouldMapAnswerDtoToAnswer() {
    // Given
    when(questionMapper.toEntity(questionDto)).thenReturn(question);

    // When
    Answer mappedEntity = answerMapper.toEntity(answerDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getAnswerId()).isEqualTo(answerDto.getId());
    assertThat(mappedEntity.getQuestion()).isEqualTo(question);
    assertThat(mappedEntity.getIsCorrect()).isEqualTo(answerDto.getIsCorrect());
    assertThat(mappedEntity.getContent()).isEqualTo(answerDto.getContent());
  }

  @Test
  void shouldHandleNullValuesForEntityToDto() {
    // When
    AnswerDto mappedDto = answerMapper.toAnswerDto(null);

    // Then
    assertThat(mappedDto).isNull();
  }

  @Test
  void shouldHandleNullValuesForDtoToEntity() {
    // When
    Answer mappedEntity = answerMapper.toEntity(null);

    // Then
    assertThat(mappedEntity).isNull();
  }

  @Test
  void shouldHandlePartialNullValuesForEntityToDto() {
    // Given
    Answer partialAnswer = Answer.builder()
        .answerId(2L)
        .isCorrect(false)
        .content(null)
        .build();

    // When
    AnswerDto mappedDto = answerMapper.toAnswerDto(partialAnswer);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getId()).isEqualTo(2L);
    assertThat(mappedDto.getIsCorrect()).isFalse();
    assertThat(mappedDto.getContent()).isNull();
    assertThat(mappedDto.getQuestion()).isNull();
  }

  @Test
  void shouldHandlePartialNullValuesForDtoToEntity() {
    // Given
    AnswerDto partialAnswerDto = AnswerDto.builder()
        .id(2L)
        .isCorrect(false)
        .content(null)
        .build();

    // When
    Answer mappedEntity = answerMapper.toEntity(partialAnswerDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getAnswerId()).isEqualTo(2L);
    assertThat(mappedEntity.getIsCorrect()).isFalse();
    assertThat(mappedEntity.getContent()).isNull();
    assertThat(mappedEntity.getQuestion()).isNull();
  }
}
