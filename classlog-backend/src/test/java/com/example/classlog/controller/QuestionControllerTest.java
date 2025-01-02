package com.example.classlog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.example.classlog.dto.AnswerDto;
import com.example.classlog.dto.FileDto;
import com.example.classlog.dto.QuestionDto;
import com.example.classlog.dto.QuestionWithAnswersDto;
import com.example.classlog.entity.QuestionType;
import com.example.classlog.service.QuestionService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class QuestionControllerTest {

  @Mock
  private QuestionService questionService;

  @InjectMocks
  private QuestionController questionController;

  private QuestionDto questionDto;
  private QuestionWithAnswersDto questionWithAnswersDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    FileDto fileDto = FileDto.builder()
        .fileId(1L)
        .filePath("test/path")
        .build();

    questionDto = QuestionDto.builder()
        .questionId(1L)
        .questionType(new QuestionType(1L, "MULTIPLE_CHOICE"))
        .editedAt(LocalDateTime.now())
        .points(10)
        .content("Sample question content")
        .file(fileDto)
        .build();

    AnswerDto answer1 = AnswerDto.builder()
        .id(1L)
        .content("Answer 1")
        .isCorrect(true)
        .build();

    AnswerDto answer2 = AnswerDto.builder()
        .id(2L)
        .content("Answer 2")
        .isCorrect(false)
        .build();

    questionWithAnswersDto = QuestionWithAnswersDto.builder()
        .question(questionDto)
        .answers(Arrays.asList(answer1, answer2))
        .build();
  }

  @Test
  void createQuestion() {
    // Given
    Mockito.when(questionService.createQuestion(any(QuestionDto.class))).thenReturn(questionDto);

    // When
    ResponseEntity<QuestionDto> response = questionController.createQuestion(questionDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(questionDto.getQuestionId(), response.getBody().getQuestionId());
  }

  @Test
  void getQuestionById() {
    // Given
    Mockito.when(questionService.getQuestionById(1L)).thenReturn(questionDto);

    // When
    ResponseEntity<QuestionDto> response = questionController.getQuestionById(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(questionDto.getQuestionId(), response.getBody().getQuestionId());
  }

  @Test
  void getQuestionsWithAnswers() {
    // Given
    List<QuestionWithAnswersDto> questionsWithAnswers = Arrays.asList(questionWithAnswersDto);
    Mockito.when(questionService.getQuestionsWithAnswers(1L)).thenReturn(questionsWithAnswers);

    // When
    ResponseEntity<List<QuestionWithAnswersDto>> response = questionController.getQuestionsWithAnswers(
        1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void deleteQuestion() {
    // When
    ResponseEntity<Void> response = questionController.deleteQuestion(1L);

    // Then
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  void getAllQuestions() {
    // Given
    List<QuestionDto> questions = Arrays.asList(questionDto, questionDto);
    Mockito.when(questionService.getAllQuestions()).thenReturn(questions);

    // When
    ResponseEntity<List<QuestionDto>> response = questionController.getAllQuestions();

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
  }

  @Test
  void createQuestionWithAnswers() {
    // Given
    List<QuestionDto> questions = Arrays.asList(questionDto, questionDto);
    Mockito.when(questionService.createQuestionWithAnswers(any(List.class), eq(1L)))
        .thenReturn(questions);

    // When
    ResponseEntity<List<QuestionDto>> response = questionController.createQuestionWithAnswers(1L,
        Arrays.asList(questionWithAnswersDto));

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
  }

  @Test
  void assignQuestionsToTask() {
    // Given
    List<QuestionDto> questions = Arrays.asList(questionDto, questionDto);
    Mockito.when(questionService.assignQuestionsToTask(any(List.class), eq(1L)))
        .thenReturn(questions);

    // When
    ResponseEntity<List<QuestionDto>> response = questionController.assignQuestionsToTask(1L,
        Arrays.asList(1L, 2L));

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
  }

  @Test
  void getAllQuestionsEmpty() {
    // Given
    Mockito.when(questionService.getAllQuestions()).thenReturn(Collections.emptyList());

    // When
    ResponseEntity<List<QuestionDto>> response = questionController.getAllQuestions();

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, response.getBody().size());
  }
}
