package com.example.classlog.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {

  private Long id; // Task ID
  private String taskName; // Name of the task
  private String description; // Description of the task
  private LocalDateTime dueDate; // Due date of the task
  private LocalDateTime createdAt; // Creation timestamp
  private UserDto createdBy; // ID of the user who created the task
  private Integer score; // Score of the task
}
