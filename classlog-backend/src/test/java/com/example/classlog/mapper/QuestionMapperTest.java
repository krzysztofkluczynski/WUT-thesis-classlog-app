package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.FileDto;
import com.example.classlog.dto.QuestionDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entity.Class;
import com.example.classlog.entity.File;
import com.example.classlog.entity.Question;
import com.example.classlog.entity.QuestionType;
import com.example.classlog.entity.Role;
import com.example.classlog.entity.User;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class QuestionMapperTest {

  @Mock
  private FileMapper fileMapper;

  private QuestionMapperImpl questionMapper;

  private QuestionType questionType;
  private File file;
  private FileDto fileDto;
  private Question question;
  private QuestionDto questionDto;

  @BeforeEach
  void setUp() throws Exception {
    // Initialize mocks
    MockitoAnnotations.openMocks(this);

    // Manually instantiate the mapper
    questionMapper = new QuestionMapperImpl();

    // Use reflection to inject the mocked FileMapper into the QuestionMapperImpl
    Field fileMapperField = QuestionMapperImpl.class.getDeclaredField("fileMapper");
    fileMapperField.setAccessible(true);
    fileMapperField.set(questionMapper, fileMapper);

    // Initialize test data
    questionType = QuestionType.builder()
        .questionTypeId(1L)
        .typeName("Multiple Choice")
        .build();

    file = File.builder()
        .fileId(1L)
        .filePath("files/document.pdf")
        .classEntity(Class.builder()
            .id(1L)
            .name("Test Class")
            .description("A test class description")
            .code("TEST123")
            .createdAt(LocalDateTime.now())
            .build())
        .userEntity(User.builder()
            .id(1L)
            .name("John")
            .surname("Doe")
            .email("john.doe@example.com")
            .password("securePassword123")
            .createdAt(LocalDateTime.now())
            .role(Role.builder()
                .id(1L)
                .roleName("Student")
                .build())
            .build())
        .createdAt(LocalDateTime.now())
        .build();

    fileDto = FileDto.builder()
        .fileId(1L)
        .filePath("files/document.pdf")
        .assignedClass(ClassDto.builder()
            .id(1L)
            .name("Test Class")
            .description("A test class description")
            .code("TEST123")
            .createdAt(LocalDateTime.now())
            .build())
        .uploadedBy(UserDto.builder()
            .id(1L)
            .name("John")
            .surname("Doe")
            .email("john.doe@example.com")
            .build())
        .createdAt(LocalDateTime.now())
        .build();

    question = Question.builder()
        .questionId(1L)
        .questionType(questionType)
        .content("What is Java?")
        .points(5)
        .file(file)
        .editedAt(LocalDateTime.now())
        .build();

    questionDto = QuestionDto.builder()
        .questionId(1L)
        .questionType(questionType)
        .content("What is Java?")
        .points(5)
        .file(fileDto)
        .editedAt(LocalDateTime.now())
        .build();
  }

  @Test
  void shouldMapQuestionToQuestionDto() {
    // Mock file mapping
    when(fileMapper.toFileDto(file)).thenReturn(fileDto);

    // When
    QuestionDto mappedDto = questionMapper.toQuestionDto(question);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getQuestionId()).isEqualTo(question.getQuestionId());
    assertThat(mappedDto.getQuestionType()).isEqualTo(question.getQuestionType());
    assertThat(mappedDto.getContent()).isEqualTo(question.getContent());
    assertThat(mappedDto.getPoints()).isEqualTo(question.getPoints());
    assertThat(mappedDto.getFile()).isEqualTo(fileDto);
  }

  @Test
  void shouldMapQuestionDtoToQuestion() {
    // Mock file mapping
    when(fileMapper.toEntity(fileDto)).thenReturn(file);

    // When
    Question mappedEntity = questionMapper.toEntity(questionDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getQuestionId()).isEqualTo(questionDto.getQuestionId());
    assertThat(mappedEntity.getQuestionType()).isEqualTo(questionDto.getQuestionType());
    assertThat(mappedEntity.getContent()).isEqualTo(questionDto.getContent());
    assertThat(mappedEntity.getPoints()).isEqualTo(questionDto.getPoints());
    assertThat(mappedEntity.getFile()).isEqualTo(file);
  }

  @Test
  void shouldHandleNullValuesForEntityToDto() {
    // Given
    Question nullQuestion = Question.builder().build();

    // When
    QuestionDto mappedDto = questionMapper.toQuestionDto(nullQuestion);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getQuestionId()).isNull();
    assertThat(mappedDto.getQuestionType()).isNull();
    assertThat(mappedDto.getContent()).isNull();
    assertThat(mappedDto.getPoints()).isNull();
    assertThat(mappedDto.getFile()).isNull();
  }

  @Test
  void shouldHandleNullValuesForDtoToEntity() {
    // Given
    QuestionDto nullQuestionDto = QuestionDto.builder().build();

    // When
    Question mappedEntity = questionMapper.toEntity(nullQuestionDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getQuestionId()).isNull();
    assertThat(mappedEntity.getQuestionType()).isNull();
    assertThat(mappedEntity.getContent()).isNull();
    assertThat(mappedEntity.getPoints()).isNull();
    assertThat(mappedEntity.getFile()).isNull();
  }
}
