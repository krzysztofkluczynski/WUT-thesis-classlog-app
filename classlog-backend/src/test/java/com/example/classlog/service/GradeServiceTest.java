package com.example.classlog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.GradeDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entity.Class;
import com.example.classlog.entity.Grade;
import com.example.classlog.entity.User;
import com.example.classlog.mapper.GradeMapper;
import com.example.classlog.repository.ClassRepository;
import com.example.classlog.repository.GradeRepository;
import com.example.classlog.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GradeServiceTest {

  @Mock
  private GradeRepository gradeRepository;

  @Mock
  private ClassRepository classRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private GradeMapper gradeMapper;

  @InjectMocks
  private GradeService gradeService;

  private Grade grade;
  private GradeDto gradeDto;
  private Class assignedClass;
  private User student;
  private User teacher;

  private ArgumentCaptor<Grade> gradeCaptor;
  private ArgumentCaptor<Long> idCaptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    assignedClass = Class.builder()
        .id(1L)
        .name("Test Class")
        .build();

    student = User.builder()
        .id(2L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    teacher = User.builder()
        .id(3L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .build();

    grade = Grade.builder()
        .gradeId(1L)
        .assignedClass(assignedClass)
        .student(student)
        .teacher(teacher)
        .grade(90)
        .wage(10)
        .description("Test Grade")
        .createdAt(LocalDateTime.now())
        .build();

    gradeDto = GradeDto.builder()
        .gradeId(1L)
        .assignedClass(ClassDto.builder().id(1L).build())
        .student(UserDto.builder().id(2L).build())
        .teacher(UserDto.builder().id(3L).build())
        .grade(90)
        .wage(10)
        .description("Test Grade")
        .createdAt(LocalDateTime.now())
        .build();

    gradeCaptor = ArgumentCaptor.forClass(Grade.class);
    idCaptor = ArgumentCaptor.forClass(Long.class);
  }

  @Test
  void shouldFindGradesByUserId() {
    // Given
    when(gradeRepository.findByStudent_Id(2L)).thenReturn(List.of(grade));
    when(gradeMapper.toGradeDto(grade)).thenReturn(gradeDto);

    // When
    List<GradeDto> result = gradeService.findGradesByUserId(2L);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getGrade()).isEqualTo(90);
    verify(gradeRepository, times(1)).findByStudent_Id(2L);
  }

  @Test
  void shouldSaveGrade() {
    // Given
    when(userRepository.findById(2L)).thenReturn(Optional.of(student));
    when(userRepository.findById(3L)).thenReturn(Optional.of(teacher));
    when(classRepository.findById(1L)).thenReturn(Optional.of(assignedClass));
    when(gradeMapper.toEntity(gradeDto)).thenReturn(grade);
    when(gradeRepository.save(grade)).thenReturn(grade);
    when(gradeMapper.toGradeDto(grade)).thenReturn(gradeDto);

    // When
    GradeDto result = gradeService.saveGrade(gradeDto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getGrade()).isEqualTo(90);
    verify(gradeRepository, times(1)).save(gradeCaptor.capture());

    Grade capturedGrade = gradeCaptor.getValue();
    assertThat(capturedGrade.getGrade()).isEqualTo(90);
    assertThat(capturedGrade.getStudent().getId()).isEqualTo(2L);
    assertThat(capturedGrade.getTeacher().getId()).isEqualTo(3L);
    assertThat(capturedGrade.getAssignedClass().getId()).isEqualTo(1L);
  }

  @Test
  void shouldFindGradesByClassId() {
    // Given
    when(gradeRepository.findByAssignedClass_IdOrderByCreatedAtDesc(1L)).thenReturn(List.of(grade));
    when(gradeMapper.toGradeDto(grade)).thenReturn(gradeDto);

    // When
    List<GradeDto> result = gradeService.findGradesByClassId(1L);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getGrade()).isEqualTo(90);
    verify(gradeRepository, times(1)).findByAssignedClass_IdOrderByCreatedAtDesc(1L);
  }

  @Test
  void shouldDeleteGrade() {
    // When
    gradeService.deleteGrade(1L);

    // Then
    verify(gradeRepository, times(1)).deleteById(idCaptor.capture());

    Long capturedId = idCaptor.getValue();
    assertThat(capturedId).isEqualTo(1L);
  }

  @Test
  void shouldThrowExceptionWhenStudentNotFound() {
    // Given
    when(userRepository.findById(2L)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> gradeService.saveGrade(gradeDto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Student with the given ID does not exist");
  }

  @Test
  void shouldThrowExceptionWhenTeacherNotFound() {
    // Given
    when(userRepository.findById(2L)).thenReturn(Optional.of(student));
    when(userRepository.findById(3L)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> gradeService.saveGrade(gradeDto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Teacher with the given ID does not exist");
  }

  @Test
  void shouldThrowExceptionWhenClassNotFound() {
    // Given
    when(userRepository.findById(2L)).thenReturn(Optional.of(student));
    when(userRepository.findById(3L)).thenReturn(Optional.of(teacher));
    when(classRepository.findById(1L)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> gradeService.saveGrade(gradeDto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Class with the given ID does not exist");
  }
}
