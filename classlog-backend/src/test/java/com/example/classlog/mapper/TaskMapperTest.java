package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.TaskDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.Task;
import com.example.classlog.entities.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TaskMapperTest {

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private TaskMapperImpl taskMapper; // Inject UserMapper into the MapStruct-generated TaskMapperImpl

  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Mock User and UserDto
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

    // Mock UserMapper behavior
    when(userMapper.toUserDto(user)).thenReturn(userDto);
    when(userMapper.toUser(userDto)).thenReturn(user);
  }

  @Test
  void shouldMapTaskToTaskDto() {
    // Given
    Task task = Task.builder()
        .id(1L)
        .taskName("Sample Task")
        .description("This is a sample task.")
        .dueDate(LocalDateTime.now().plusDays(1))
        .createdAt(LocalDateTime.now())
        .createdBy(user)
        .score(100)
        .build();

    // When
    TaskDto taskDto = taskMapper.toTaskDto(task);

    // Then
    assertThat(taskDto).isNotNull();
    assertThat(taskDto.getId()).isEqualTo(1L);
    assertThat(taskDto.getTaskName()).isEqualTo("Sample Task");
    assertThat(taskDto.getCreatedBy()).isEqualTo(userDto);
  }

  @Test
  void shouldMapTaskDtoToTask() {
    // Given
    TaskDto taskDto = TaskDto.builder()
        .id(1L)
        .taskName("Sample Task")
        .description("This is a sample task.")
        .dueDate(LocalDateTime.now().plusDays(1))
        .createdAt(LocalDateTime.now())
        .createdBy(userDto)
        .score(100)
        .build();

    // When
    Task task = taskMapper.toEntity(taskDto);

    // Then
    assertThat(task).isNotNull();
    assertThat(task.getId()).isEqualTo(1L);
    assertThat(task.getTaskName()).isEqualTo("Sample Task");
    assertThat(task.getCreatedBy()).isEqualTo(user);
  }

  @Test
  void shouldMapTaskToTaskDtoWithNullValues() {
    // Given
    Task task = Task.builder()
        .id(1L)
        .taskName(null)
        .description(null)
        .dueDate(null)
        .createdBy(null)
        .score(null)
        .build();

    // When
    TaskDto taskDto = taskMapper.toTaskDto(task);

    // Then
    assertThat(taskDto).isNotNull();
    assertThat(taskDto.getTaskName()).isNull();
    assertThat(taskDto.getCreatedBy()).isNull();
  }

  @Test
  void shouldMapTaskDtoToTaskWithNullValues() {
    // Given
    TaskDto taskDto = TaskDto.builder()
        .id(1L)
        .taskName(null)
        .description(null)
        .dueDate(null)
        .createdBy(null)
        .score(null)
        .build();

    // When
    Task task = taskMapper.toEntity(taskDto);

    // Then
    assertThat(task).isNotNull();
    assertThat(task.getTaskName()).isNull();
    assertThat(task.getCreatedBy()).isNull();
  }
}
