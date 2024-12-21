package com.example.classlog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.CreateClassDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.Class;
import com.example.classlog.entities.User;
import com.example.classlog.mapper.ClassMapper;
import com.example.classlog.repository.ClassRepository;
import com.example.classlog.repository.UserClassRepository;
import com.example.classlog.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ClassServiceTest {

  @Mock
  private ClassRepository classRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserClassRepository userClassRepository;

  @Mock
  private ClassMapper classMapper;

  @InjectMocks
  private ClassService classService;

  private Class classEntity;
  private User user;
  private ClassDto classDto;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Initialize test data
    classEntity = Class.builder()
        .id(1L)
        .name("Test Class")
        .description("Test Description")
        .code("TEST123")
        .build();

    user = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    classDto = ClassDto.builder()
        .id(1L)
        .name("Test Class")
        .description("Test Description")
        .code("TEST123")
        .build();

    userDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();
  }

  @Test
  void shouldFindClassesByUserId() {
    // Given
    when(classRepository.findByUserId(1L)).thenReturn(List.of(classEntity));
    when(classMapper.toClassDto(classEntity)).thenReturn(classDto);

    // When
    List<ClassDto> result = classService.findClassesByUserId(1L);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("Test Class");
  }

  @Test
  void shouldReturnEmptyListWhenNoClassesFoundForUser() {
    // Given
    Long userId = 1L;
    when(classRepository.findByUserId(userId)).thenReturn(List.of()); // Simulate no classes found

    // When
    List<ClassDto> result = classService.findClassesByUserId(userId);

    // Then
    assertThat(result).isEmpty();
  }


  @Test
  void shouldAddUsersToClass() {
    // Given
    when(userClassRepository.existsByClassEntity_IdAndUser_Id(1L, 1L)).thenReturn(false);

    // When
    classService.addUsersToClass(1L, List.of(userDto));

    // Then
    ArgumentCaptor<Long> classIdCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

    verify(userClassRepository, times(1)).insertUserIntoClass(classIdCaptor.capture(),
        userIdCaptor.capture());

    assertThat(classIdCaptor.getValue()).isEqualTo(1L);
    assertThat(userIdCaptor.getValue()).isEqualTo(1L);
  }

  @Test
  void shouldNotAddDuplicateUsersToClass() {
    // Given
    List<UserDto> users = List.of(userDto);
    when(userClassRepository.existsByClassEntity_IdAndUser_Id(eq(1L), eq(1L))).thenReturn(true);

    // When
    classService.addUsersToClass(1L, users);

    // Then
    verify(userClassRepository, never()).insertUserIntoClass(any(), any());
  }

  @Test
  void shouldRemoveUsersFromClass() {
    // Given
    when(userClassRepository.existsByClassEntity_IdAndUser_Id(1L, 1L)).thenReturn(true);

    // When
    classService.removeUsersFromClass(1L, List.of(userDto));

    // Then
    ArgumentCaptor<Long> classIdCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

    verify(userClassRepository).deleteUserFromClass(classIdCaptor.capture(),
        userIdCaptor.capture());

    assertThat(classIdCaptor.getValue()).isEqualTo(1L);
    assertThat(userIdCaptor.getValue()).isEqualTo(1L);
  }

  @Test
  void shouldNotRemoveNonExistingUsersFromClass() {
    // Given
    List<UserDto> users = List.of(userDto);
    when(userClassRepository.existsByClassEntity_IdAndUser_Id(eq(1L), eq(1L))).thenReturn(false);

    // When
    classService.removeUsersFromClass(1L, users);

    // Then
    verify(userClassRepository, never()).deleteUserFromClass(any(), any());
  }

  @Test
  void shouldFindClassById() {
    // Given
    when(classRepository.findById(1L)).thenReturn(Optional.of(classEntity));
    when(classMapper.toClassDto(classEntity)).thenReturn(classDto);

    // When
    ClassDto result = classService.findClassById(1L);

    // Then
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(classRepository).findById(idCaptor.capture());

    assertThat(idCaptor.getValue()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Test Class");
  }

  @Test
  void shouldReturnNullWhenClassNotFoundById() {
    // Given
    when(classRepository.findById(1L)).thenReturn(Optional.empty());

    // When
    ClassDto result = classService.findClassById(1L);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void shouldAddUserToClassByCode() {
    // Given
    when(classRepository.findByCode("TEST123")).thenReturn(Optional.of(classEntity));
    when(userClassRepository.existsByClassEntity_IdAndUser_Id(eq(1L), eq(1L))).thenReturn(false);

    // When
    classService.addUserToClassByCode("TEST123", userDto);

    // Then
    verify(userClassRepository, times(1)).insertUserIntoClass(1L, 1L);
  }

  @Test
  void shouldThrowExceptionWhenClassNotFoundByCode() {
    // Given
    when(classRepository.findByCode("TEST123")).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> classService.addUserToClassByCode("TEST123", userDto))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("Class with code TEST123 not found");
  }

  @Test
  void shouldThrowExceptionWhenUserAlreadyInClass() {
    // Given
    when(classRepository.findByCode("TEST123")).thenReturn(Optional.of(classEntity));
    when(userClassRepository.existsByClassEntity_IdAndUser_Id(eq(1L), eq(1L))).thenReturn(true);

    // When & Then
    assertThatThrownBy(() -> classService.addUserToClassByCode("TEST123", userDto))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("User is already added to that class");
  }

  @Test
  void shouldAddClassAndAssociateUser() {
    // Given
    CreateClassDto createClassDto = CreateClassDto.builder()
        .classDto(classDto)
        .createdBy(userDto)
        .build();

    when(classMapper.toEntity(any())).thenReturn(classEntity);
    when(classRepository.save(any())).thenReturn(classEntity);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(classMapper.toClassDto(classEntity)).thenReturn(classDto);

    // When
    ClassDto result = classService.addClass(createClassDto);

    // Then
    ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class);
    ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

    verify(classRepository).save(classCaptor.capture());
    verify(userClassRepository).insertUserIntoClass(userIdCaptor.capture(), eq(1L));

    assertThat(classCaptor.getValue().getName()).isEqualTo("Test Class");
    assertThat(userIdCaptor.getValue()).isEqualTo(1L);
  }

  @Test
  void shouldThrowExceptionWhenCreatorNotFound() {
    // Given
    CreateClassDto createClassDto = CreateClassDto.builder()
        .classDto(classDto)
        .createdBy(userDto)
        .build();

    when(classMapper.toEntity(any())).thenReturn(classEntity);
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> classService.addClass(createClassDto))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("User not found");
  }

  @Test
  void shouldThrowExceptionWhenClassCodeNotFound() {
    // Given
    when(classRepository.findByCode("INVALID_CODE")).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> classService.addUserToClassByCode("INVALID_CODE", userDto))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("Class with code INVALID_CODE not found");
  }

  @Test
  void shouldThrowExceptionWhenCreatorNotFoundInAddClass() {
    // Given
    CreateClassDto createClassDto = CreateClassDto.builder()
        .classDto(classDto)
        .createdBy(userDto)
        .build();

    when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> classService.addClass(createClassDto))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("User not found");
  }


}
