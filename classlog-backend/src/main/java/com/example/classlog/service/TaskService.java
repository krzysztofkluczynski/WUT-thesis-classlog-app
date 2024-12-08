package com.example.classlog.service;


import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.TaskDto;
import com.example.classlog.entities.Task;
import com.example.classlog.entities.User;
import com.example.classlog.entities.UserTask;
import com.example.classlog.mapper.TaskMapper;
import com.example.classlog.repository.TaskRepository;
import com.example.classlog.repository.UserRepository;
import com.example.classlog.repository.UserTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final UserTaskRepository userTaskRepository;

    private final UserService userService;

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toTaskDto)
                .collect(java.util.stream.Collectors.toList());
    }

    public Optional<TaskDto> getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toTaskDto);
    }

    public TaskDto createTask(TaskDto task) {
        Task savedTask = taskRepository.save(taskMapper.toEntity(task));
        return taskMapper.toTaskDto(savedTask);
    }

    public Optional<Task> updateTask(Long id, Task taskDetails) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isEmpty()) {
            return Optional.empty();
        }

        Task task = taskOptional.get();
        task.setTaskName(taskDetails.getTaskName());
        task.setDescription(taskDetails.getDescription());
        task.setDueDate(taskDetails.getDueDate());

        Task updatedTask = taskRepository.save(task);
        return Optional.of(updatedTask);
    }

    public boolean deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }
        taskRepository.deleteById(id);
        return true;
    }

    public boolean assignUsersToTask(Long taskId, List<ClassDto> classDtos) {
        // Retrieve the task
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isEmpty()) {
            return false;
        }

        Task task = taskOptional.get();

        // Use a Set to collect unique users
        Set<Long> uniqueUserIds = new HashSet<>();
        List<User> uniqueUsers = classDtos.stream()
                .flatMap(classDto -> userService.getUsersByClass(classDto.getId()).stream()) // Get users from each class
                .filter(userDto -> userDto.getRole() != null && userDto.getRole().getId() == 2) // Filter by roleId = 2
                .filter(userDto -> uniqueUserIds.add(userDto.getId())) // Add to Set; returns false if already present
                .map(userDto -> userRepository.findById(userDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("User with ID " + userDto.getId() + " not found"))) // Convert UserDto to User
                .collect(Collectors.toList());

        // Assign unique users to the task
        uniqueUsers.forEach(user -> {
            UserTask userTask = new UserTask();
            userTask.setTask(task);
            userTask.setUser(user);
            userTask.setScore(0);
            userTaskRepository.save(userTask);
        });

        return true;
    }


    public List<TaskDto> getTasksByCreatedBy(Long id) {
        return taskRepository.findByCreatedBy_IdOrderByCreatedAtDesc(id).stream()
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByUser(Long id) {
        List<UserTask> userTasks = userTaskRepository.findByUser_Id(id);
        return userTasks.stream()
                .map(UserTask::getTask)
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByUserCurrent(Long id) {
        LocalDateTime now = LocalDateTime.now(); // Pobierz aktualną datę i czas
        List<UserTask> userTasks = userTaskRepository.findByUser_Id(id);

        return userTasks.stream()
                .filter(userTask -> userTask.getTask().getDueDate().isAfter(now)) // Filtruj zadania z dueDate > now
                .map(UserTask::getTask)
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksTodoByUser(Long userId) {
        return taskRepository.findTasksTodoByUser(userId).stream()
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksDoneByUser(Long userId) {
        return taskRepository.findTasksDoneByUser(userId).stream()
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getOverdueTasksNotSubmittedByUser(Long userId) {
        return taskRepository.findOverdueTasksNotSubmittedByUser(userId).stream()
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }
}