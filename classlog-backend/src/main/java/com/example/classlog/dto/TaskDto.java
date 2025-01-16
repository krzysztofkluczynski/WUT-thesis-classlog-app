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

  private Long id;
  private String taskName;
  private String description;
  private LocalDateTime dueDate;
  private LocalDateTime createdAt;
  private UserDto createdBy;
  private Integer score;
}
