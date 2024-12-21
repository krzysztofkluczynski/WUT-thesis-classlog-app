package com.example.classlog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.User;
import com.example.classlog.mapper.UserMapper;
import com.example.classlog.repository.PresenceRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PresenceServiceTest {

  @Mock
  private PresenceRepository presenceRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private PresenceService presenceService;

  private User user1;
  private User user2;
  private UserDto userDto1;
  private UserDto userDto2;

  private final long lessonId = 1L;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    user1 = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .build();

    user2 = User.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .build();

    userDto1 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .build();

    userDto2 = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Smith")
        .build();
  }

  @Test
  void shouldAddPresenceForListOfStudents() {
    // Given
    List<UserDto> userDtos = List.of(userDto1, userDto2);
    when(presenceRepository.existsByLesson_LessonIdAndStudent_Id(lessonId, 1L)).thenReturn(false);
    when(presenceRepository.existsByLesson_LessonIdAndStudent_Id(lessonId, 2L)).thenReturn(true);

    // When
    presenceService.addPresenceForListOfStudents(lessonId, userDtos);

    // Then
    verify(presenceRepository, times(1)).insertPresence(lessonId, 1L);
    verify(presenceRepository, times(0)).insertPresence(lessonId, 2L);
  }

  @Test
  void shouldGetPresentStudents() {
    // Given
    List<User> users = List.of(user1, user2);
    when(presenceRepository.findUsersByLessonId(lessonId)).thenReturn(users);
    when(userMapper.toUserDto(user1)).thenReturn(userDto1);
    when(userMapper.toUserDto(user2)).thenReturn(userDto2);

    // When
    List<UserDto> result = presenceService.getPresentStudents(lessonId);

    // Then
    assertThat(result).hasSize(2);
    assertThat(result).containsExactlyInAnyOrder(userDto1, userDto2);
    verify(presenceRepository, times(1)).findUsersByLessonId(lessonId);
  }

  @Test
  void shouldUpdatePresenceForLesson() {
    // Given
    List<Long> currentStudentIds = List.of(1L, 2L); // Existing students
    List<Long> updatedStudentIds = List.of(2L, 3L); // New presence list
    when(presenceRepository.findStudentIdsByLesson_LessonId(lessonId)).thenReturn(
        currentStudentIds);

    // When
    presenceService.updatePresenceForLesson(lessonId, updatedStudentIds);

    // Then
    // Verifying deletions for students no longer present
    verify(presenceRepository, times(1)).deleteByLesson_LessonIdAndStudent_Id(lessonId, 1L);
    verify(presenceRepository, times(0)).deleteByLesson_LessonIdAndStudent_Id(lessonId, 2L);

    // Verifying additions for new students
    verify(presenceRepository, times(0)).insertPresence(lessonId, 2L);
    verify(presenceRepository, times(1)).insertPresence(lessonId, 3L);
  }

  @Test
  void shouldHandleEmptyPresenceForUpdate() {
    // Given
    List<Long> currentStudentIds = List.of(1L, 2L);
    List<Long> updatedStudentIds = Collections.emptyList(); // No students are present
    when(presenceRepository.findStudentIdsByLesson_LessonId(lessonId)).thenReturn(
        currentStudentIds);

    // When
    presenceService.updatePresenceForLesson(lessonId, updatedStudentIds);

    // Then
    // Verifying all students are removed
    verify(presenceRepository, times(1)).deleteByLesson_LessonIdAndStudent_Id(lessonId, 1L);
    verify(presenceRepository, times(1)).deleteByLesson_LessonIdAndStudent_Id(lessonId, 2L);
    verify(presenceRepository, times(0)).insertPresence(eq(lessonId), anyLong());
  }

  @Test
  void shouldHandleEmptyInitialPresenceForUpdate() {
    // Given
    List<Long> currentStudentIds = Collections.emptyList(); // No current presence
    List<Long> updatedStudentIds = List.of(3L, 4L); // New students are present
    when(presenceRepository.findStudentIdsByLesson_LessonId(lessonId)).thenReturn(
        currentStudentIds);

    // When
    presenceService.updatePresenceForLesson(lessonId, updatedStudentIds);

    // Then
    // Verifying new students are added
    verify(presenceRepository, times(1)).insertPresence(lessonId, 3L);
    verify(presenceRepository, times(1)).insertPresence(lessonId, 4L);
    verify(presenceRepository, times(0)).deleteByLesson_LessonIdAndStudent_Id(eq(lessonId),
        anyLong());
  }
}
