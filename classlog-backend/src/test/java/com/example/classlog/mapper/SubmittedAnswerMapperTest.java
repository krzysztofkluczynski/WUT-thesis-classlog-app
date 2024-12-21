package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.SubmittedAnswerDto;
import com.example.classlog.dto.TaskQuestionDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.SubmittedAnswer;
import com.example.classlog.entities.TaskQuestion;
import com.example.classlog.entities.User;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SubmittedAnswerMapperTest {

  @Mock
  private TaskQuestionMapper taskQuestionMapper;

  @Mock
  private UserMapper userMapper;

  private SubmittedAnswerMapperImpl submittedAnswerMapper;

  private SubmittedAnswer submittedAnswer;
  private SubmittedAnswerDto submittedAnswerDto;

  private TaskQuestion taskQuestion;
  private TaskQuestionDto taskQuestionDto;

  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // Manually instantiate the SubmittedAnswerMapper and inject dependencies
    submittedAnswerMapper = new SubmittedAnswerMapperImpl();

    Field taskQuestionMapperField = SubmittedAnswerMapperImpl.class.getDeclaredField(
        "taskQuestionMapper");
    taskQuestionMapperField.setAccessible(true);
    taskQuestionMapperField.set(submittedAnswerMapper, taskQuestionMapper);

    Field userMapperField = SubmittedAnswerMapperImpl.class.getDeclaredField("userMapper");
    userMapperField.setAccessible(true);
    userMapperField.set(submittedAnswerMapper, userMapper);

    // Initialize test data
    taskQuestion = TaskQuestion.builder()
        .taskQuestionId(1L)
        .build();

    taskQuestionDto = TaskQuestionDto.builder()
        .taskQuestionId(1L)
        .build();

    user = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    userDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    submittedAnswer = SubmittedAnswer.builder()
        .submittedAnswerId(1L)
        .taskQuestion(taskQuestion)
        .user(user)
        .createdAt(LocalDateTime.now())
        .content("Sample answer")
        .build();

    submittedAnswerDto = SubmittedAnswerDto.builder()
        .submittedAnswerId(1L)
        .taskQuestion(taskQuestionDto)
        .user(userDto)
        .createdAt(LocalDateTime.now())
        .content("Sample answer")
        .build();
  }

  @Test
  void shouldMapSubmittedAnswerToDto() {
    // Given
    when(taskQuestionMapper.toDto(taskQuestion)).thenReturn(taskQuestionDto);
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    SubmittedAnswerDto mappedDto = submittedAnswerMapper.toDto(submittedAnswer);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getSubmittedAnswerId()).isEqualTo(submittedAnswer.getSubmittedAnswerId());
    assertThat(mappedDto.getTaskQuestion()).isEqualTo(taskQuestionDto);
    assertThat(mappedDto.getUser()).isEqualTo(userDto);
    assertThat(mappedDto.getCreatedAt()).isEqualTo(submittedAnswer.getCreatedAt());
    assertThat(mappedDto.getContent()).isEqualTo(submittedAnswer.getContent());
  }

  @Test
  void shouldMapDtoToSubmittedAnswer() {
    // Given
    when(taskQuestionMapper.toEntity(taskQuestionDto)).thenReturn(taskQuestion);
    when(userMapper.toUser(userDto)).thenReturn(user);

    // When
    SubmittedAnswer mappedEntity = submittedAnswerMapper.toEntity(submittedAnswerDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getSubmittedAnswerId()).isEqualTo(
        submittedAnswerDto.getSubmittedAnswerId());
    assertThat(mappedEntity.getTaskQuestion()).isEqualTo(taskQuestion);
    assertThat(mappedEntity.getUser()).isEqualTo(user);
    assertThat(mappedEntity.getCreatedAt()).isEqualTo(submittedAnswerDto.getCreatedAt());
    assertThat(mappedEntity.getContent()).isEqualTo(submittedAnswerDto.getContent());
  }

  @Test
  void shouldHandleNullValuesForEntityToDto() {
    // When
    SubmittedAnswerDto mappedDto = submittedAnswerMapper.toDto(null);

    // Then
    assertThat(mappedDto).isNull();
  }

  @Test
  void shouldHandleNullValuesForDtoToEntity() {
    // When
    SubmittedAnswer mappedEntity = submittedAnswerMapper.toEntity(null);

    // Then
    assertThat(mappedEntity).isNull();
  }

  @Test
  void shouldHandlePartialNullValuesForEntityToDto() {
    // Given
    SubmittedAnswer partialEntity = SubmittedAnswer.builder()
        .submittedAnswerId(2L)
        .content(null)
        .build();

    // When
    SubmittedAnswerDto mappedDto = submittedAnswerMapper.toDto(partialEntity);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getSubmittedAnswerId()).isEqualTo(2L);
    assertThat(mappedDto.getContent()).isNull();
    assertThat(mappedDto.getTaskQuestion()).isNull();
    assertThat(mappedDto.getUser()).isNull();
  }

  @Test
  void shouldHandlePartialNullValuesForDtoToEntity() {
    // Given
    SubmittedAnswerDto partialDto = SubmittedAnswerDto.builder()
        .submittedAnswerId(2L)
        .content(null)
        .build();

    // When
    SubmittedAnswer mappedEntity = submittedAnswerMapper.toEntity(partialDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getSubmittedAnswerId()).isEqualTo(2L);
    assertThat(mappedEntity.getContent()).isNull();
    assertThat(mappedEntity.getTaskQuestion()).isNull();
    assertThat(mappedEntity.getUser()).isNull();
  }
}
