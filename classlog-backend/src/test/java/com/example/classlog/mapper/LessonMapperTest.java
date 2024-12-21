package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.LessonDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.Class;
import com.example.classlog.entities.Lesson;
import com.example.classlog.entities.Role;
import com.example.classlog.entities.User;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LessonMapperTest {

  @Mock
  private UserMapper userMapper;

  @Mock
  private ClassMapper classMapper;

  private LessonMapperImpl lessonMapper;

  private Lesson lesson;
  private LessonDto lessonDto;
  private User createdBy;
  private UserDto createdByUserDto;
  private Class lessonClass;
  private ClassDto lessonClassDto;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // Manually instantiate the LessonMapper and inject dependencies
    lessonMapper = new LessonMapperImpl();

    Field userMapperField = LessonMapperImpl.class.getDeclaredField("userMapper");
    userMapperField.setAccessible(true);
    userMapperField.set(lessonMapper, userMapper);

    Field classMapperField = LessonMapperImpl.class.getDeclaredField("classMapper");
    classMapperField.setAccessible(true);
    classMapperField.set(lessonMapper, classMapper);

    // Initialize entities and DTOs
    createdBy = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .role(Role.builder().id(2L).roleName("Teacher").build())
        .createdAt(LocalDateTime.now())
        .build();

    createdByUserDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .role(createdBy.getRole())
        .createdAt(LocalDateTime.now())
        .build();

    lessonClass = Class.builder()
        .id(1L)
        .name("Math 101")
        .description("Basic Math")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();

    lessonClassDto = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Basic Math")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();

    lesson = Lesson.builder()
        .lessonId(1L)
        .createdBy(createdBy)
        .classEntity(lessonClass)
        .lessonDate(LocalDateTime.of(2024, 12, 20, 10, 0))
        .subject("Algebra Basics")
        .content("Introduction to Algebra")
        .build();

    lessonDto = LessonDto.builder()
        .lessonId(1L)
        .createdByUser(createdByUserDto)
        .lessonClass(lessonClassDto)
        .lessonDate(LocalDateTime.of(2024, 12, 20, 10, 0))
        .subject("Algebra Basics")
        .content("Introduction to Algebra")
        .build();
  }

  @Test
  void shouldMapLessonToLessonDto() {
    // Given
    when(userMapper.toUserDto(createdBy)).thenReturn(createdByUserDto);
    when(classMapper.toClassDto(lessonClass)).thenReturn(lessonClassDto);

    // When
    LessonDto mappedDto = lessonMapper.toLessonDto(lesson);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getLessonId()).isEqualTo(lesson.getLessonId());
    assertThat(mappedDto.getCreatedByUser()).isEqualTo(createdByUserDto);
    assertThat(mappedDto.getLessonClass()).isEqualTo(lessonClassDto);
    assertThat(mappedDto.getLessonDate()).isEqualTo(lesson.getLessonDate());
    assertThat(mappedDto.getSubject()).isEqualTo(lesson.getSubject());
    assertThat(mappedDto.getContent()).isEqualTo(lesson.getContent());
  }

  @Test
  void shouldMapLessonDtoToLesson() {
    // Given
    when(userMapper.toUser(createdByUserDto)).thenReturn(createdBy);
    when(classMapper.toEntity(lessonClassDto)).thenReturn(lessonClass);

    // When
    Lesson mappedEntity = lessonMapper.toEntity(lessonDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getLessonId()).isEqualTo(lessonDto.getLessonId());
    assertThat(mappedEntity.getCreatedBy()).isEqualTo(createdBy);
    assertThat(mappedEntity.getClassEntity()).isEqualTo(lessonClass);
    assertThat(mappedEntity.getLessonDate()).isEqualTo(lessonDto.getLessonDate());
    assertThat(mappedEntity.getSubject()).isEqualTo(lessonDto.getSubject());
    assertThat(mappedEntity.getContent()).isEqualTo(lessonDto.getContent());
  }

  @Test
  void shouldHandleNullValuesForEntityToDto() {
    // When
    LessonDto mappedDto = lessonMapper.toLessonDto(null);

    // Then
    assertThat(mappedDto).isNull();
  }

  @Test
  void shouldHandleNullValuesForDtoToEntity() {
    // When
    Lesson mappedEntity = lessonMapper.toEntity(null);

    // Then
    assertThat(mappedEntity).isNull();
  }

  @Test
  void shouldHandlePartialNullValuesForEntityToDto() {
    // Given
    Lesson partialLesson = Lesson.builder()
        .lessonId(2L)
        .lessonDate(LocalDateTime.of(2024, 12, 21, 10, 0))
        .subject("Geometry Basics")
        .build();

    // When
    LessonDto mappedDto = lessonMapper.toLessonDto(partialLesson);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getLessonId()).isEqualTo(2L);
    assertThat(mappedDto.getLessonDate()).isEqualTo(LocalDateTime.of(2024, 12, 21, 10, 0));
    assertThat(mappedDto.getSubject()).isEqualTo("Geometry Basics");
    assertThat(mappedDto.getCreatedByUser()).isNull();
    assertThat(mappedDto.getLessonClass()).isNull();
    assertThat(mappedDto.getContent()).isNull();
  }

  @Test
  void shouldHandlePartialNullValuesForDtoToEntity() {
    // Given
    LessonDto partialLessonDto = LessonDto.builder()
        .lessonId(2L)
        .lessonDate(LocalDateTime.of(2024, 12, 21, 10, 0))
        .subject("Geometry Basics")
        .build();

    // When
    Lesson mappedEntity = lessonMapper.toEntity(partialLessonDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getLessonId()).isEqualTo(2L);
    assertThat(mappedEntity.getLessonDate()).isEqualTo(LocalDateTime.of(2024, 12, 21, 10, 0));
    assertThat(mappedEntity.getSubject()).isEqualTo("Geometry Basics");
    assertThat(mappedEntity.getCreatedBy()).isNull();
    assertThat(mappedEntity.getClassEntity()).isNull();
    assertThat(mappedEntity.getContent()).isNull();
  }
}
