package com.example.classlog.controller;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.SubmittedTaskDto;
import com.example.classlog.dto.TaskDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.Task;
import com.example.classlog.entities.User;
import com.example.classlog.entities.UserTask;
import com.example.classlog.repository.TaskRepository;
import com.example.classlog.repository.UserRepository;
import com.example.classlog.repository.UserTaskRepository;
import com.example.classlog.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    // Standard GET to retrieve all tasks
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // Standard GET to retrieve a task by ID
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

    // Endpoint 1: All tasks assigned to the user
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

    // Endpoint 3: Tasks with records in submitted_answer
    @GetMapping("/assignedToUser/{id}/submitted")
    public ResponseEntity<List<TaskDto>> getCurrentTasksDoneByUser(@PathVariable Long id) {
        List<TaskDto> tasks = taskService.getTasksDoneByUser(id);
        return ResponseEntity.ok(tasks);
    }

    // Endpoint 4: Tasks with dueDate < now and no record in submitted_answer
    @GetMapping("/assignedToUser/{id}/overdue/notSubmitted")
    public ResponseEntity<List<TaskDto>> getOverdueTasksNotSubmittedByUser(@PathVariable Long id) {
        List<TaskDto> tasks = taskService.getOverdueTasksNotSubmittedByUser(id);
        return ResponseEntity.ok(tasks);
    }


    // Standard POST to create a new task
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

    // Standard DELETE to remove a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskService.deleteTask(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // Custom POST to assign users to a task
    @PostMapping("/{taskId}/assign-users")
    public ResponseEntity<Void> assignUsersToTask(@PathVariable Long taskId, @RequestBody List<ClassDto> classDtos) {
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
}

