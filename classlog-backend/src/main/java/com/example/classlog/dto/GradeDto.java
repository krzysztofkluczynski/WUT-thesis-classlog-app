package com.example.classlog.dto;

import com.example.classlog.entities.Class;
import com.example.classlog.entities.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GradeDto {
    private Long gradeId;
    private ClassDto assignedClass;   // Full Class object
    private UserDto student;          // Full User object for the student
    private UserDto teacher;          // Full User object for the teacher
    private Integer grade;
    private Integer wage;
    private String description;
    private LocalDateTime createdAt;
}