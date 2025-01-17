package com.example.classlog.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GradeDto {

  private Long gradeId;
  private ClassDto assignedClass;
  private UserDto student;
  private UserDto teacher;
  private Integer grade;
  private Integer wage;
  private String description;
  private LocalDateTime createdAt;
}