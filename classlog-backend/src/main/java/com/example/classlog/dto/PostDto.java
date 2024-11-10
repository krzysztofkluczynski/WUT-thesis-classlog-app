package com.example.classlog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private ClassDto assignedClass;
    private UserDto user;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}