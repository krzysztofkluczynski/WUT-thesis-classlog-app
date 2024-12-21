package com.example.classlog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.LessonDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.Class;
import com.example.classlog.entities.Lesson;
import com.example.classlog.entities.User;
import com.example.classlog.mapper.LessonMapper;
import com.example.classlog.repository.LessonRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LessonServiceTest {

  @Mock
  private LessonRepository lessonRepository;

  @Mock
  private LessonMapper lessonMapper;

  @InjectMocks
  private LessonService lessonService;

  private Lesson lesson;
  private LessonDto lessonDto;
  private Class lessonClass;
  private User createdByUser;

  private ArgumentCaptor<Long> idCaptor;
  private ArgumentCaptor<Lesson> lessonCaptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    lessonClass = Class.builder()
        .id(1L)
        .name("Test Class")
        .build();

    createdByUser = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    lesson = Lesson.builder()
        .lessonId(1L)
        .createdBy(createdByUser)
        .classEntity(lessonClass)
        .lessonDate(LocalDateTime.now())
        .subject("Math")
        .content("Algebra lesson")
        .build();

    lessonDto = LessonDto.builder()
        .lessonId(1L)
        .createdByUser(UserDto.builder().id(1L).build())
        .lessonClass(ClassDto.builder().id(1L).build())
        .lessonDate(LocalDateTime.now())
        .subject("Math")
        .content("Algebra lesson")
        .build();

    idCaptor = ArgumentCaptor.forClass(Long.class);
    lessonCaptor = ArgumentCaptor.forClass(Lesson.class);
  }

  @Test
  void shouldGetAllLessons() {
    when(lessonRepository.findAll()).thenReturn(List.of(lesson));
    when(lessonMapper.toLessonDto(lesson)).thenReturn(lessonDto);

    List<LessonDto> result = lessonService.getAllLessons();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getSubject()).isEqualTo("Math");
    verify(lessonRepository, times(1)).findAll();
  }

  @Test
  void shouldGetLessonById() {
    when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
    when(lessonMapper.toLessonDto(lesson)).thenReturn(lessonDto);

    LessonDto result = lessonService.getLessonById(1L);

    assertThat(result).isNotNull();
    assertThat(result.getSubject()).isEqualTo("Math");
    verify(lessonRepository, times(1)).findById(idCaptor.capture());
    assertThat(idCaptor.getValue()).isEqualTo(1L);
  }

  @Test
  void shouldReturnNullWhenLessonByIdNotFound() {
    when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

    LessonDto result = lessonService.getLessonById(1L);

    assertThat(result).isNull();
    verify(lessonRepository, times(1)).findById(idCaptor.capture());
    assertThat(idCaptor.getValue()).isEqualTo(1L);
  }

  @Test
  void shouldCreateLesson() {
    when(lessonMapper.toEntity(lessonDto)).thenReturn(lesson);
    when(lessonRepository.save(lesson)).thenReturn(lesson);
    when(lessonMapper.toLessonDto(lesson)).thenReturn(lessonDto);

    LessonDto result = lessonService.createLesson(lessonDto);

    assertThat(result).isNotNull();
    assertThat(result.getSubject()).isEqualTo("Math");
    verify(lessonRepository, times(1)).save(lessonCaptor.capture());

    Lesson capturedLesson = lessonCaptor.getValue();
    assertThat(capturedLesson.getSubject()).isEqualTo("Math");
  }

  @Test
  void shouldDeleteLesson() {
    lessonService.deleteLesson(1L);

    verify(lessonRepository, times(1)).deleteById(idCaptor.capture());
    assertThat(idCaptor.getValue()).isEqualTo(1L);
  }

  @Test
  void shouldGetRecentLessonsCreatedBy() {
    when(lessonRepository.findByCreatedBy_IdOrderByLessonDateDesc(1L)).thenReturn(List.of(lesson));
    when(lessonMapper.toLessonDto(lesson)).thenReturn(lessonDto);

    List<LessonDto> result = lessonService.getRecentLessonsCreatedBy(1L, 1);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getSubject()).isEqualTo("Math");
    verify(lessonRepository, times(1)).findByCreatedBy_IdOrderByLessonDateDesc(1L);
  }

  @Test
  void shouldReturnEmptyListWhenNoRecentLessonsFound() {
    when(lessonRepository.findByCreatedBy_IdOrderByLessonDateDesc(1L)).thenReturn(
        Collections.emptyList());

    List<LessonDto> result = lessonService.getRecentLessonsCreatedBy(1L, 1);

    assertThat(result).isEmpty();
    verify(lessonRepository, times(1)).findByCreatedBy_IdOrderByLessonDateDesc(1L);
  }

  @Test
  void shouldUpdateLesson() {
    when(lessonMapper.toEntity(lessonDto)).thenReturn(lesson);
    when(lessonRepository.save(lesson)).thenReturn(lesson);
    when(lessonMapper.toLessonDto(lesson)).thenReturn(lessonDto);

    LessonDto result = lessonService.updateLesson(lessonDto);

    assertThat(result).isNotNull();
    assertThat(result.getSubject()).isEqualTo("Math");
    verify(lessonRepository, times(1)).save(lessonCaptor.capture());

    Lesson capturedLesson = lessonCaptor.getValue();
    assertThat(capturedLesson.getSubject()).isEqualTo("Math");
  }

  @Test
  void shouldGetAllLessonsCreatedBy() {
    when(lessonRepository.findByCreatedBy_IdOrderByLessonDateDesc(1L)).thenReturn(List.of(lesson));
    when(lessonMapper.toLessonDto(lesson)).thenReturn(lessonDto);

    List<LessonDto> result = lessonService.getAllLessonsCreatedBy(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getSubject()).isEqualTo("Math");
    verify(lessonRepository, times(1)).findByCreatedBy_IdOrderByLessonDateDesc(1L);
  }

  @Test
  void shouldGetAllLessonsForUser() {
    when(lessonRepository.findLessonsByUserId(1L)).thenReturn(List.of(lesson));
    when(lessonMapper.toLessonDto(lesson)).thenReturn(lessonDto);

    List<LessonDto> result = lessonService.getAllLessonsForUser(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getSubject()).isEqualTo("Math");
    verify(lessonRepository, times(1)).findLessonsByUserId(1L);
  }

  @Test
  void shouldGetRecentLessonsForUser() {
    when(lessonRepository.findLessonsByUserId(1L)).thenReturn(List.of(lesson));
    when(lessonMapper.toLessonDto(lesson)).thenReturn(lessonDto);

    List<LessonDto> result = lessonService.getRecentLessonsForUser(1L, 1);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getSubject()).isEqualTo("Math");
    verify(lessonRepository, times(1)).findLessonsByUserId(1L);
  }

  @Test
  void shouldGetLessonByClassId() {
    when(lessonRepository.findByClassEntity_Id(1L)).thenReturn(List.of(lesson));
    when(lessonMapper.toLessonDto(lesson)).thenReturn(lessonDto);

    List<LessonDto> result = lessonService.getLessonByClassId(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getSubject()).isEqualTo("Math");
    verify(lessonRepository, times(1)).findByClassEntity_Id(1L);
  }

  @Test
  void shouldGetRecentLessonsByClassId() {
    when(lessonRepository.findByClassEntity_Id(1L)).thenReturn(List.of(lesson));
    when(lessonMapper.toLessonDto(lesson)).thenReturn(lessonDto);

    List<LessonDto> result = lessonService.getRecentLessonsByClassId(1L, 1);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getSubject()).isEqualTo("Math");
    verify(lessonRepository, times(1)).findByClassEntity_Id(1L);
  }
}
