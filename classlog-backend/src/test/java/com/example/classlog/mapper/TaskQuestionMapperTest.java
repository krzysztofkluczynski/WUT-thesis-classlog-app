package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.QuestionDto;
import com.example.classlog.dto.TaskDto;
import com.example.classlog.dto.TaskQuestionDto;
import com.example.classlog.entity.Question;
import com.example.classlog.entity.Task;
import com.example.classlog.entity.TaskQuestion;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TaskQuestionMapperTest {

  @Mock
  private TaskMapper taskMapper;

  @Mock
  private QuestionMapper questionMapper;

  private TaskQuestionMapperImpl taskQuestionMapper;

  private Task task;
  private TaskDto taskDto;
  private Question question;
  private QuestionDto questionDto;
  private TaskQuestion taskQuestion;
  private TaskQuestionDto taskQuestionDto;

  @BeforeEach
  void setUp() throws Exception {
    // Initialize mocks
    MockitoAnnotations.openMocks(this);

    // Manually instantiate the mapper
    taskQuestionMapper = new TaskQuestionMapperImpl();

    // Use reflection to inject the mocked TaskMapper and QuestionMapper into TaskQuestionMapperImpl
    Field taskMapperField = TaskQuestionMapperImpl.class.getDeclaredField("taskMapper");
    taskMapperField.setAccessible(true);
    taskMapperField.set(taskQuestionMapper, taskMapper);

    Field questionMapperField = TaskQuestionMapperImpl.class.getDeclaredField("questionMapper");
    questionMapperField.setAccessible(true);
    questionMapperField.set(taskQuestionMapper, questionMapper);

    // Initialize test data
    task = Task.builder()
        .id(1L)
        .taskName("Sample Task")
        .description("Test task description")
        .createdAt(LocalDateTime.now())
        .build();

    taskDto = TaskDto.builder()
        .id(1L)
        .taskName("Sample Task")
        .description("Test task description")
        .createdAt(LocalDateTime.now())
        .build();

    question = Question.builder()
        .questionId(1L)
        .content("What is Java?")
        .points(5)
        .editedAt(LocalDateTime.now())
        .build();

    questionDto = QuestionDto.builder()
        .questionId(1L)
        .content("What is Java?")
        .points(5)
        .editedAt(LocalDateTime.now())
        .build();

    taskQuestion = TaskQuestion.builder()
        .taskQuestionId(1L)
        .task(task)
        .question(question)
        .build();

    taskQuestionDto = TaskQuestionDto.builder()
        .taskQuestionId(1L)
        .task(taskDto)
        .question(questionDto)
        .build();
  }

  @Test
  void shouldMapTaskQuestionToTaskQuestionDto() {
    // Mock TaskMapper and QuestionMapper mappings
    when(taskMapper.toTaskDto(task)).thenReturn(taskDto);
    when(questionMapper.toQuestionDto(question)).thenReturn(questionDto);

    // When
    TaskQuestionDto mappedDto = taskQuestionMapper.toDto(taskQuestion);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getTaskQuestionId()).isEqualTo(taskQuestion.getTaskQuestionId());
    assertThat(mappedDto.getTask()).isEqualTo(taskDto);
    assertThat(mappedDto.getQuestion()).isEqualTo(questionDto);
  }

  @Test
  void shouldMapTaskQuestionDtoToTaskQuestion() {
    // Mock TaskMapper and QuestionMapper mappings
    when(taskMapper.toEntity(taskDto)).thenReturn(task);
    when(questionMapper.toEntity(questionDto)).thenReturn(question);

    // When
    TaskQuestion mappedEntity = taskQuestionMapper.toEntity(taskQuestionDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getTaskQuestionId()).isEqualTo(taskQuestionDto.getTaskQuestionId());
    assertThat(mappedEntity.getTask()).isEqualTo(task);
    assertThat(mappedEntity.getQuestion()).isEqualTo(question);
  }

  @Test
  void shouldHandleNullValuesForEntityToDto() {
    // Given
    TaskQuestion nullTaskQuestion = TaskQuestion.builder().build();

    // When
    TaskQuestionDto mappedDto = taskQuestionMapper.toDto(nullTaskQuestion);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getTaskQuestionId()).isNull();
    assertThat(mappedDto.getTask()).isNull();
    assertThat(mappedDto.getQuestion()).isNull();
  }

  @Test
  void shouldHandleNullValuesForDtoToEntity() {
    // Given
    TaskQuestionDto nullTaskQuestionDto = TaskQuestionDto.builder().build();

    // When
    TaskQuestion mappedEntity = taskQuestionMapper.toEntity(nullTaskQuestionDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getTaskQuestionId()).isNull();
    assertThat(mappedEntity.getTask()).isNull();
    assertThat(mappedEntity.getQuestion()).isNull();
  }
}
