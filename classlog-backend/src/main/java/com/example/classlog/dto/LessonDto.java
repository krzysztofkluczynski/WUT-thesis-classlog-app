package com.example.classlog.dto;

import com.example.classlog.entities.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LessonDto {
    private Long lessonId;
    private UserDto createdByUser;
    private ClassDto lessonClass;
    private LocalDateTime lessonDate;
    private String subject;
    private String content;
}