package com.example.classlog.controller;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.SubmittedTaskDto;
import com.example.classlog.dto.TaskDto;
import com.example.classlog.dto.UserTaskDto;
import com.example.classlog.entity.Task;
import com.example.classlog.service.TaskService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

  private final TaskService taskService;

  @GetMapping
  public ResponseEntity<List<TaskDto>> getAllTasks() {
    List<TaskDto> tasks = taskService.getAllTasks();
    return ResponseEntity.ok(tasks);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
    Optional<TaskDto> task = taskService.getTaskById(id);
    return task.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/createdBy/{id}")
  public ResponseEntity<List<TaskDto>> getTasksByCreatedBy(@PathVariable Long id) {
    List<TaskDto> tasks = taskService.getTasksByCreatedBy(id);
    return ResponseEntity.ok(tasks);
  }

  @GetMapping("/assignedToUser/{id}")
  public ResponseEntity<List<TaskDto>> getAllTasksAssignedToUser(@PathVariable Long id) {
    List<TaskDto> tasks = taskService.getTasksByUser(id);
    return ResponseEntity.ok(tasks);
  }

  // Endpoint 2: Tasks with dueDate > now and no record in submitted_answer
  @GetMapping("/assignedToUser/{id}/current/notSubmitted")
  public ResponseEntity<List<TaskDto>> getCurrentTasksTodoByUser(@PathVariable Long id) {
    List<TaskDto> tasks = taskService.getTasksTodoByUser(id);
    return ResponseEntity.ok(tasks);
  }

  @GetMapping("/assignedToUser/{id}/submitted")
  public ResponseEntity<List<TaskDto>> getCurrentTasksDoneByUser(@PathVariable Long id) {
    List<TaskDto> tasks = taskService.getTasksDoneByUser(id);
    return ResponseEntity.ok(tasks);
  }

  @GetMapping("/assignedToUser/{id}/overdue/notSubmitted")
  public ResponseEntity<List<TaskDto>> getOverdueTasksNotSubmittedByUser(@PathVariable Long id) {
    List<TaskDto> tasks = taskService.getOverdueTasksNotSubmittedByUser(id);
    return ResponseEntity.ok(tasks);
  }


  @PostMapping
  public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto task) {
    TaskDto savedTask = taskService.createTask(task);
    return ResponseEntity.ok(savedTask);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
    Optional<Task> updatedTask = taskService.updateTask(id, taskDetails);
    return updatedTask.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    if (!taskService.deleteTask(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{taskId}/assign-users")
  public ResponseEntity<Void> assignUsersToTask(@PathVariable Long taskId,
      @RequestBody List<ClassDto> classDtos) {
    if (!taskService.assignUsersToTask(taskId, classDtos)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().build();
  }

  @PostMapping("/submit")
  public ResponseEntity<Void> submitTask(@RequestBody SubmittedTaskDto submittedTaskDto) {
    boolean isSuccess = taskService.processSubmittedTask(submittedTaskDto);
    if (isSuccess) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{taskId}/user/{userId}/submitted")
  public ResponseEntity<SubmittedTaskDto> getSubmittedTaskDetails(
      @PathVariable Long taskId,
      @PathVariable Long userId) {
    Optional<SubmittedTaskDto> submittedTask = taskService.getSubmittedTaskDetails(taskId, userId);
    return submittedTask.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/createdBy/{id}/submitted")
  public ResponseEntity<List<UserTaskDto>> getSubmitttedTasksCreatedByUser(@PathVariable Long id) {
    List<UserTaskDto> tasks = taskService.getTasksCreatedByUserSubmitted(id);
    return ResponseEntity.ok(tasks);
  }

  @GetMapping("/createdBy/{id}/overdue/notSubmitted")
  public ResponseEntity<List<UserTaskDto>> getOverdueTasksNotSubmittedCreatedByUser(
      @PathVariable Long id) {
    List<UserTaskDto> tasks = taskService.getOverdueTasksNotSubmitttedCreatedByUser(id);
    return ResponseEntity.ok(tasks);
  }

  @PutMapping("/user/{userId}/task/{taskId}/score")
  public ResponseEntity<?> updateScore(
      @PathVariable Long userId,
      @PathVariable Long taskId,
      @RequestBody Map<String, Integer> payload) {
    Integer newScore = payload.get("newScore");
    boolean isUpdated = taskService.updateUserTaskScore(userId, taskId, newScore);
    if (isUpdated) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }


}

