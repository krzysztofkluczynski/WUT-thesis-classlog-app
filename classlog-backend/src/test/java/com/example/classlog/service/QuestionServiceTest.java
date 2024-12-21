package com.example.classlog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.AnswerDto;
import com.example.classlog.dto.QuestionDto;
import com.example.classlog.dto.QuestionWithAnswersDto;
import com.example.classlog.entities.Answer;
import com.example.classlog.entities.Question;
import com.example.classlog.entities.Task;
import com.example.classlog.entities.TaskQuestion;
import com.example.classlog.mapper.AnswerMapper;
import com.example.classlog.mapper.QuestionMapper;
import com.example.classlog.repository.AnswerRepository;
import com.example.classlog.repository.QuestionRepository;
import com.example.classlog.repository.TaskQuestionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class QuestionServiceTest {

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private AnswerRepository answerRepository;

  @Mock
  private TaskQuestionRepository taskQuestionRepository;

  @Mock
  private QuestionMapper questionMapper;

  @Mock
  private AnswerMapper answerMapper;

  @InjectMocks
  private QuestionService questionService;

  private Question question;
  private QuestionDto questionDto;
  private Answer answer;
  private AnswerDto answerDto;
  private TaskQuestion taskQuestion;
  private ArgumentCaptor<Question> questionCaptor;
  private ArgumentCaptor<List<Answer>> answerCaptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    question = Question.builder()
        .questionId(1L)
        .content("What is the capital of France?")
        .points(10)
        .build();

    questionDto = QuestionDto.builder()
        .questionId(1L)
        .content("What is the capital of France?")
        .points(10)
        .build();

    answer = Answer.builder()
        .answerId(1L)
        .content("Paris")
        .isCorrect(true)
        .question(question)
        .build();

    answerDto = AnswerDto.builder()
        .id(1L)
        .content("Paris")
        .isCorrect(true)
        .question(questionDto)
        .build();

    taskQuestion = TaskQuestion.builder()
        .task(Task.builder().id(1L).build())
        .question(question)
        .build();

    questionCaptor = ArgumentCaptor.forClass(Question.class);
    answerCaptor = ArgumentCaptor.forClass((Class) List.class);
  }

  @Test
  void shouldCreateQuestion() {
    // Given
    when(questionMapper.toEntity(questionDto)).thenReturn(question);
    when(questionRepository.save(question)).thenReturn(question);
    when(questionMapper.toQuestionDto(question)).thenReturn(questionDto);

    // When
    QuestionDto result = questionService.createQuestion(questionDto);

    // Then
    verify(questionRepository, times(1)).save(questionCaptor.capture());
    Question capturedQuestion = questionCaptor.getValue();
    assertThat(capturedQuestion.getContent()).isEqualTo("What is the capital of France?");
    assertThat(result).isEqualTo(questionDto);
  }

  @Test
  void shouldGetQuestionById() {
    // Given
    when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
    when(questionMapper.toQuestionDto(question)).thenReturn(questionDto);

    // When
    QuestionDto result = questionService.getQuestionById(1L);

    // Then
    assertThat(result).isEqualTo(questionDto);
    verify(questionRepository, times(1)).findById(1L);
  }

  @Test
  void shouldDeleteQuestion() {
    // When
    questionService.deleteQuestion(1L);

    // Then
    verify(questionRepository, times(1)).deleteById(1L);
  }

  @Test
  void shouldGetAllQuestions() {
    // Given
    when(questionRepository.findAll()).thenReturn(List.of(question));
    when(questionMapper.toQuestionDto(question)).thenReturn(questionDto);

    // When
    List<QuestionDto> result = questionService.getAllQuestions();

    // Then
    assertThat(result).containsExactly(questionDto);
    verify(questionRepository, times(1)).findAll();
  }

  @Test
  void shouldCreateQuestionWithAnswers() {
    // Given
    QuestionWithAnswersDto questionWithAnswersDto = QuestionWithAnswersDto.builder()
        .question(questionDto)
        .answers(List.of(answerDto))
        .build();

    when(questionMapper.toEntity(questionDto)).thenReturn(question);
    when(questionRepository.save(question)).thenReturn(question);
    when(answerMapper.toEntity(answerDto)).thenReturn(answer);

    // When
    List<QuestionDto> result = questionService.createQuestionWithAnswers(
        List.of(questionWithAnswersDto), 1L);

    // Then
    verify(questionRepository, times(1)).save(questionCaptor.capture());
    verify(answerRepository, times(1)).saveAll(answerCaptor.capture());
    assertThat(result).hasSize(1);
  }

  @Test
  void shouldAssignQuestionsToTask() {
    // Given
    when(questionRepository.findAllById(List.of(1L))).thenReturn(List.of(question));
    when(questionMapper.toQuestionDto(question)).thenReturn(questionDto);

    // When
    List<QuestionDto> result = questionService.assignQuestionsToTask(List.of(1L), 1L);

    // Then
    assertThat(result).containsExactly(questionDto);
    verify(taskQuestionRepository, times(1)).saveAll(any());
  }

  @Test
  void shouldGetQuestionsWithAnswers() {
    // Given
    when(taskQuestionRepository.findAllByTaskId(1L)).thenReturn(List.of(taskQuestion));
    when(answerRepository.findAllByQuestion_QuestionId(1L)).thenReturn(List.of(answer));
    when(questionMapper.toQuestionDto(question)).thenReturn(questionDto);
    when(answerMapper.toAnswerDto(answer)).thenReturn(answerDto);

    // When
    List<QuestionWithAnswersDto> result = questionService.getQuestionsWithAnswers(1L);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getQuestion()).isEqualTo(questionDto);
    assertThat(result.get(0).getAnswers()).containsExactly(answerDto);
  }
}
