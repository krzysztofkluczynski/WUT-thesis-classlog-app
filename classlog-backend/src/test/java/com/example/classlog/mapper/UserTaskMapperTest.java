package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.TaskDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.dto.UserTaskDto;
import com.example.classlog.entity.Task;
import com.example.classlog.entity.User;
import com.example.classlog.entity.UserTask;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserTaskMapperTest {

  @Mock
  private UserMapper userMapper;

  @Mock
  private TaskMapper taskMapper;

  private UserTaskMapperImpl userTaskMapper;

  private User user;
  private UserDto userDto;
  private Task task;
  private TaskDto taskDto;
  private UserTask userTask;
  private UserTaskDto userTaskDto;

  @BeforeEach
  void setUp() throws Exception {
    // Initialize mocks
    MockitoAnnotations.openMocks(this);

    // Manually instantiate the mapper
    userTaskMapper = new UserTaskMapperImpl();

    // Use reflection to inject the mocked UserMapper and TaskMapper into UserTaskMapperImpl
    Field userMapperField = UserTaskMapperImpl.class.getDeclaredField("userMapper");
    userMapperField.setAccessible(true);
    userMapperField.set(userTaskMapper, userMapper);

    Field taskMapperField = UserTaskMapperImpl.class.getDeclaredField("taskMapper");
    taskMapperField.setAccessible(true);
    taskMapperField.set(userTaskMapper, taskMapper);

    // Initialize test data
    user = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .role(null) // Set role if necessary
        .createdAt(LocalDateTime.now())
        .build();

    userDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .role(null) // Set role if necessary
        .createdAt(LocalDateTime.now())
        .build();

    task = Task.builder()
        .id(1L)
        .taskName("Sample Task")
        .description("Test task description")
        .dueDate(LocalDateTime.now().plusDays(7))
        .createdAt(LocalDateTime.now())
        .createdBy(user)
        .score(10)
        .build();

    taskDto = TaskDto.builder()
        .id(1L)
        .taskName("Sample Task")
        .description("Test task description")
        .dueDate(LocalDateTime.now().plusDays(7))
        .createdAt(LocalDateTime.now())
        .createdBy(userDto)
        .score(10)
        .build();

    userTask = UserTask.builder()
        .id(1L)
        .user(user)
        .task(task)
        .score(8)
        .build();

    userTaskDto = UserTaskDto.builder()
        .userTaskId(1L)
        .user(userDto)
        .task(taskDto)
        .score(8)
        .build();
  }

  @Test
  void shouldMapUserTaskToUserTaskDto() {
    // Mock UserMapper and TaskMapper mappings
    when(userMapper.toUserDto(user)).thenReturn(userDto);
    when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

    // When
    UserTaskDto mappedDto = userTaskMapper.toDto(userTask);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getUserTaskId()).isEqualTo(userTask.getId());
    assertThat(mappedDto.getUser()).isEqualTo(userDto);
    assertThat(mappedDto.getTask()).isEqualTo(taskDto);
    assertThat(mappedDto.getScore()).isEqualTo(userTask.getScore());
  }

  @Test
  void shouldMapUserTaskDtoToUserTask() {
    // Mock UserMapper and TaskMapper mappings
    when(userMapper.toUser(userDto)).thenReturn(user);
    when(taskMapper.toEntity(taskDto)).thenReturn(task);

    // When
    UserTask mappedEntity = userTaskMapper.toEntity(userTaskDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getId()).isEqualTo(userTaskDto.getUserTaskId());
    assertThat(mappedEntity.getUser()).isEqualTo(user);
    assertThat(mappedEntity.getTask()).isEqualTo(task);
    assertThat(mappedEntity.getScore()).isEqualTo(userTaskDto.getScore());
  }

  @Test
  void shouldHandleNullValuesForEntityToDto() {
    // Given
    UserTask nullUserTask = UserTask.builder().build();

    // When
    UserTaskDto mappedDto = userTaskMapper.toDto(nullUserTask);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getUserTaskId()).isNull();
    assertThat(mappedDto.getUser()).isNull();
    assertThat(mappedDto.getTask()).isNull();
    assertThat(mappedDto.getScore()).isNull();
  }

  @Test
  void shouldHandleNullValuesForDtoToEntity() {
    // Given
    UserTaskDto nullUserTaskDto = UserTaskDto.builder().build();

    // When
    UserTask mappedEntity = userTaskMapper.toEntity(nullUserTaskDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getId()).isNull();
    assertThat(mappedEntity.getUser()).isNull();
    assertThat(mappedEntity.getTask()).isNull();
    assertThat(mappedEntity.getScore()).isNull();
  }
}
