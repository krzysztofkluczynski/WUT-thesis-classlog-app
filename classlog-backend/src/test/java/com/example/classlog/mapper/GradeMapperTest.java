package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.GradeDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.Class;
import com.example.classlog.entities.Grade;
import com.example.classlog.entities.Role;
import com.example.classlog.entities.User;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GradeMapperTest {

  @Mock
  private ClassMapper classMapper;

  @Mock
  private UserMapper userMapper;

  private GradeMapperImpl gradeMapper;

  private Grade grade;
  private GradeDto gradeDto;

  private Class assignedClass;
  private ClassDto assignedClassDto;

  private User student;
  private UserDto studentDto;

  private User teacher;
  private UserDto teacherDto;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // Manually instantiate the GradeMapperImpl
    gradeMapper = new GradeMapperImpl();

    // Inject the ClassMapper mock into the GradeMapper
    Field classMapperField = GradeMapperImpl.class.getDeclaredField("classMapper");
    classMapperField.setAccessible(true);
    classMapperField.set(gradeMapper, classMapper);

    // Inject the UserMapper mock into the GradeMapper
    Field userMapperField = GradeMapperImpl.class.getDeclaredField("userMapper");
    userMapperField.setAccessible(true);
    userMapperField.set(gradeMapper, userMapper);

    // Initialize entities and DTOs
    assignedClass = Class.builder()
        .id(1L)
        .name("Mathematics")
        .description("Math Class")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();

    assignedClassDto = ClassDto.builder()
        .id(1L)
        .name("Mathematics")
        .description("Math Class")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();

    student = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .role(Role.builder().id(2L).roleName("Student").build())
        .createdAt(LocalDateTime.now())
        .build();

    studentDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .role(student.getRole())
        .createdAt(LocalDateTime.now())
        .build();

    teacher = User.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .role(Role.builder().id(3L).roleName("Teacher").build())
        .createdAt(LocalDateTime.now())
        .build();

    teacherDto = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .role(teacher.getRole())
        .createdAt(LocalDateTime.now())
        .build();

    grade = Grade.builder()
        .gradeId(1L)
        .assignedClass(assignedClass)
        .student(student)
        .teacher(teacher)
        .grade(95)
        .wage(10)
        .description("Excellent performance")
        .createdAt(LocalDateTime.now())
        .build();

    gradeDto = GradeDto.builder()
        .gradeId(1L)
        .assignedClass(assignedClassDto)
        .student(studentDto)
        .teacher(teacherDto)
        .grade(95)
        .wage(10)
        .description("Excellent performance")
        .createdAt(LocalDateTime.now())
        .build();
  }


  @Test
  void shouldMapGradeToGradeDto() {
    // Given
    when(classMapper.toClassDto(assignedClass)).thenReturn(assignedClassDto);
    when(userMapper.toUserDto(student)).thenReturn(studentDto);
    when(userMapper.toUserDto(teacher)).thenReturn(teacherDto);

    // When
    GradeDto mappedDto = gradeMapper.toGradeDto(grade);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getGradeId()).isEqualTo(grade.getGradeId());
    assertThat(mappedDto.getAssignedClass()).isEqualTo(assignedClassDto);
    assertThat(mappedDto.getStudent()).isEqualTo(studentDto);
    assertThat(mappedDto.getTeacher()).isEqualTo(teacherDto);
    assertThat(mappedDto.getGrade()).isEqualTo(grade.getGrade());
    assertThat(mappedDto.getWage()).isEqualTo(grade.getWage());
    assertThat(mappedDto.getDescription()).isEqualTo(grade.getDescription());
    assertThat(mappedDto.getCreatedAt()).isEqualTo(grade.getCreatedAt());
  }

  @Test
  void shouldMapGradeDtoToGrade() {
    // Given
    when(classMapper.toEntity(assignedClassDto)).thenReturn(assignedClass);
    when(userMapper.toUser(studentDto)).thenReturn(student);
    when(userMapper.toUser(teacherDto)).thenReturn(teacher);

    // When
    Grade mappedEntity = gradeMapper.toEntity(gradeDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getGradeId()).isEqualTo(gradeDto.getGradeId());
    assertThat(mappedEntity.getAssignedClass()).isEqualTo(assignedClass);
    assertThat(mappedEntity.getStudent()).isEqualTo(student);
    assertThat(mappedEntity.getTeacher()).isEqualTo(teacher);
    assertThat(mappedEntity.getGrade()).isEqualTo(gradeDto.getGrade());
    assertThat(mappedEntity.getWage()).isEqualTo(gradeDto.getWage());
    assertThat(mappedEntity.getDescription()).isEqualTo(gradeDto.getDescription());
    assertThat(mappedEntity.getCreatedAt()).isEqualTo(gradeDto.getCreatedAt());
  }

  @Test
  void shouldHandleNullValuesForEntityToDto() {
    // When
    GradeDto mappedDto = gradeMapper.toGradeDto(null);

    // Then
    assertThat(mappedDto).isNull();
  }

  @Test
  void shouldHandleNullValuesForDtoToEntity() {
    // When
    Grade mappedEntity = gradeMapper.toEntity(null);

    // Then
    assertThat(mappedEntity).isNull();
  }
}
