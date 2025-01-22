package com.example.classlog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.AnswerDto;
import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.QuestionDto;
import com.example.classlog.dto.QuestionWithUserAnswerDto;
import com.example.classlog.dto.SubmittedTaskDto;
import com.example.classlog.dto.TaskDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.dto.UserTaskDto;
import com.example.classlog.entity.Answer;
import com.example.classlog.entity.Question;
import com.example.classlog.entity.Role;
import com.example.classlog.entity.SubmittedAnswer;
import com.example.classlog.entity.Task;
import com.example.classlog.entity.TaskQuestion;
import com.example.classlog.entity.User;
import com.example.classlog.entity.UserTask;
import com.example.classlog.mapper.AnswerMapper;
import com.example.classlog.mapper.QuestionMapper;
import com.example.classlog.mapper.TaskMapper;
import com.example.classlog.mapper.UserMapper;
import com.example.classlog.mapper.UserTaskMapper;
import com.example.classlog.repository.AnswerRepository;
import com.example.classlog.repository.QuestionRepository;
import com.example.classlog.repository.SubmittedAnswerRepository;
import com.example.classlog.repository.TaskQuestionRepository;
import com.example.classlog.repository.TaskRepository;
import com.example.classlog.repository.UserRepository;
import com.example.classlog.repository.UserTaskRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TaskServiceTest {

  @Mock
  private TaskRepository taskRepository;

  @Mock
  private TaskMapper taskMapper;

  @Mock
  private AnswerMapper answerMapper;

  @Mock
  private UserMapper userMapper;

  @Mock
  private QuestionMapper questionMapper;

  @Mock
  private UserTaskMapper userTaskMapper;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserTaskRepository userTaskRepository;

  @Mock
  private AnswerRepository answerRepository;

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private TaskQuestionRepository taskQuestionRepository;

  @Mock
  private SubmittedAnswerRepository submittedAnswerRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private TaskService taskService;

  private Task task;
  private TaskDto taskDto;
  private User user;
  private UserDto userDto;
  private Question question;
  private QuestionDto questionDto;
  private Answer answer;
  private AnswerDto answerDto;
  private SubmittedTaskDto submittedTaskDto;
  private TaskQuestion taskQuestion;
  private ClassDto classDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

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

    task = Task.builder()
        .id(1L)
        .taskName("Test Task")
        .description("Test Description")
        .createdBy(user)
        .dueDate(LocalDateTime.now().plusDays(5))
        .createdAt(LocalDateTime.now())
        .build();

    taskDto = TaskDto.builder()
        .id(1L)
        .taskName("Test Task")
        .description("Test Description")
        .createdBy(userDto)
        .dueDate(LocalDateTime.now().plusDays(5))
        .createdAt(LocalDateTime.now())
        .build();

    question = Question.builder()
        .questionId(1L)
        .content("Test Question")
        .points(5)
        .build();

    questionDto = QuestionDto.builder()
        .questionId(1L)
        .content("Test Question")
        .points(5)
        .build();

    answer = Answer.builder()
        .answerId(1L)
        .content("Correct Answer")
        .isCorrect(true)
        .build();

    answerDto = AnswerDto.builder()
        .id(1L)
        .content("Correct Answer")
        .isCorrect(true)
        .build();

    taskQuestion = TaskQuestion.builder()
        .task(task)
        .question(question)
        .build();

    submittedTaskDto = SubmittedTaskDto.builder()
        .task(taskDto)
        .user(userDto)
        .questionsWithAnswers(List.of(QuestionWithUserAnswerDto.builder()
            .question(questionDto)
            .answers(List.of(answerDto))
            .userAnswer("Correct Answer")
            .score(5)
            .build()))
        .score(5)
        .build();

    classDto = ClassDto.builder()
        .id(1L)
        .name("Test Class")
        .description("Test Description")
        .build();
  }

  @Test
  void shouldGetAllTasks() {
    when(taskRepository.findAll()).thenReturn(List.of(task));
    when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

    List<TaskDto> result = taskService.getAllTasks();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTaskName()).isEqualTo("Test Task");
    verify(taskRepository, times(1)).findAll();
  }

  @Test
  void shouldCreateTask() {
    when(taskMapper.toEntity(taskDto)).thenReturn(task);
    when(taskRepository.save(task)).thenReturn(task);
    when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

    TaskDto result = taskService.createTask(taskDto);

    assertThat(result.getTaskName()).isEqualTo("Test Task");
    verify(taskRepository, times(1)).save(task);
  }

  @Test
  void shouldGetTaskById() {
    when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

    Optional<TaskDto> result = taskService.getTaskById(1L);

    assertThat(result).isPresent();
    assertThat(result.get().getTaskName()).isEqualTo("Test Task");
    verify(taskRepository, times(1)).findById(1L);
  }

  @Test
  void shouldDeleteTask() {
    when(taskRepository.existsById(1L)).thenReturn(true);

    boolean result = taskService.deleteTask(1L);

    assertThat(result).isTrue();
    verify(taskRepository, times(1)).deleteById(1L);
  }

  @Test
  void shouldNotDeleteNonExistingTask() {
    when(taskRepository.existsById(1L)).thenReturn(false);

    boolean result = taskService.deleteTask(1L);

    assertThat(result).isFalse();
    verify(taskRepository, never()).deleteById(1L);
  }

  @Test
  void shouldProcessSubmittedTask() {
    when(userTaskRepository.findByUser_IdAndTask_Id(1L, 1L)).thenReturn(
        Optional.of(UserTask.builder()
            .task(task)
            .user(user)
            .score(0)
            .build()));
    when(taskQuestionRepository.findTaskQuestionByQuestionIdAndTaskId(1L, 1L)).thenReturn(
        Optional.of(taskQuestion));

    boolean result = taskService.processSubmittedTask(submittedTaskDto);

    assertThat(result).isTrue();
    verify(submittedAnswerRepository, times(1)).save(any(SubmittedAnswer.class));
  }

  @Test
  void shouldFailToProcessNonExistingSubmittedTask() {
    when(userTaskRepository.findByUser_IdAndTask_Id(1L, 1L)).thenReturn(Optional.empty());

    boolean result = taskService.processSubmittedTask(submittedTaskDto);

    assertThat(result).isFalse();
    verify(submittedAnswerRepository, never()).save(any(SubmittedAnswer.class));
  }

  @Test
  void shouldAssignUsersToTask() {
    when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    when(userService.getUsersByClass(1L)).thenReturn(List.of(userDto));
    when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));

    userDto.setRole(new Role(2L, "Student")); // Ensure the role matches the filtering logic
    user.setRole(new Role(2L, "Student"));      // Ensure the entity matches the role as well

    boolean result = taskService.assignUsersToTask(1L, List.of(classDto));

    assertThat(result).isTrue();
    verify(userTaskRepository, times(1)).save(any(UserTask.class));
  }


  @Test
  void shouldNotAssignUsersToNonExistingTask() {
    when(taskRepository.findById(1L)).thenReturn(Optional.empty());

    boolean result = taskService.assignUsersToTask(1L, List.of(classDto));

    assertThat(result).isFalse();
    verify(userTaskRepository, never()).save(any(UserTask.class));
  }

  @Test
  void shouldUpdateTask() {
    Task updatedTaskDetails = Task.builder()
        .taskName("Updated Task")
        .description("Updated Description")
        .dueDate(LocalDateTime.now().plusDays(7))
        .build();

    when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    when(taskRepository.save(task)).thenReturn(updatedTaskDetails);

    Optional<Task> result = taskService.updateTask(1L, updatedTaskDetails);

    assertThat(result).isPresent();
    assertThat(result.get().getTaskName()).isEqualTo("Updated Task");
    verify(taskRepository, times(1)).save(task);
  }

  @Test
  void shouldNotUpdateNonExistingTask() {
    when(taskRepository.findById(1L)).thenReturn(Optional.empty());

    Optional<Task> result = taskService.updateTask(1L, task);

    assertThat(result).isEmpty();
    verify(taskRepository, never()).save(any(Task.class));
  }

  @Test
  void shouldGetTasksByCreatedBy() {
    when(taskRepository.findByCreatedBy_IdOrderByCreatedAtDesc(1L)).thenReturn(List.of(task));
    when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

    List<TaskDto> result = taskService.getTasksByCreatedBy(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTaskName()).isEqualTo("Test Task");
    verify(taskRepository, times(1)).findByCreatedBy_IdOrderByCreatedAtDesc(1L);
  }

  @Test
  void shouldGetTasksByUser() {
    when(userTaskRepository.findByUser_Id(1L)).thenReturn(
        List.of(UserTask.builder().task(task).build()));
    when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

    List<TaskDto> result = taskService.getTasksByUser(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTaskName()).isEqualTo("Test Task");
    verify(userTaskRepository, times(1)).findByUser_Id(1L);
  }

  @Test
  void shouldGetTasksTodoByUser() {
    when(taskRepository.findTasksTodoByUser(1L)).thenReturn(List.of(task));
    when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

    List<TaskDto> result = taskService.getTasksTodoByUser(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTaskName()).isEqualTo("Test Task");
    verify(taskRepository, times(1)).findTasksTodoByUser(1L);
  }

  @Test
  void shouldGetTasksDoneByUser() {
    when(taskRepository.findTasksDoneByUser(1L)).thenReturn(List.of(task));
    when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

    List<TaskDto> result = taskService.getTasksDoneByUser(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTaskName()).isEqualTo("Test Task");
    verify(taskRepository, times(1)).findTasksDoneByUser(1L);
  }

  @Test
  void shouldGetOverdueTasksNotSubmittedByUser() {
    when(taskRepository.findOverdueTasksNotSubmittedByUser(1L)).thenReturn(List.of(task));
    when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

    List<TaskDto> result = taskService.getOverdueTasksNotSubmittedByUser(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTaskName()).isEqualTo("Test Task");
    verify(taskRepository, times(1)).findOverdueTasksNotSubmittedByUser(1L);
  }

  @Test
  void shouldUpdateUserTaskScore() {
    UserTask userTask = UserTask.builder().user(user).task(task).score(0).build();
    when(userTaskRepository.findByUser_IdAndTask_Id(1L, 1L)).thenReturn(Optional.of(userTask));

    boolean result = taskService.updateUserTaskScore(1L, 1L, 10);

    assertThat(result).isTrue();
    assertThat(userTask.getScore()).isEqualTo(10);
    verify(userTaskRepository, times(1)).save(userTask);
  }

  @Test
  void shouldNotUpdateScoreForNonExistingUserTask() {
    when(userTaskRepository.findByUser_IdAndTask_Id(1L, 1L)).thenReturn(Optional.empty());

    boolean result = taskService.updateUserTaskScore(1L, 1L, 10);

    assertThat(result).isFalse();
    verify(userTaskRepository, never()).save(any(UserTask.class));
  }

  @Test
  void shouldGetTasksCreatedByUserSubmitted() {
    UserTask userTask = UserTask.builder().user(user).task(task).score(10).build();
    when(userTaskRepository.findTasksCreatedByUserWithSubmittedAnswers(1L)).thenReturn(
        List.of(userTask));
    when(userTaskMapper.toDto(userTask)).thenReturn(UserTaskDto.builder().score(10).build());

    List<UserTaskDto> result = taskService.getTasksCreatedByUserSubmitted(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getScore()).isEqualTo(10);
    verify(userTaskRepository, times(1)).findTasksCreatedByUserWithSubmittedAnswers(1L);
  }

  @Test
  void shouldGetOverdueTasksNotSubmittedCreatedByUser() {
    UserTask userTask = UserTask.builder().user(user).task(task).build();
    when(userTaskRepository.findOverdueTasksNotSubmittedByCreatedByUser(1L)).thenReturn(
        List.of(userTask));
    when(userTaskMapper.toDto(userTask)).thenReturn(UserTaskDto.builder().score(0).build());

    List<UserTaskDto> result = taskService.getOverdueTasksNotSubmitttedCreatedByUser(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getScore()).isEqualTo(0);
    verify(userTaskRepository, times(1)).findOverdueTasksNotSubmittedByCreatedByUser(1L);
  }
}
