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
public class ClassDto {
    private Long id;
    private String name;
    private String description;
    private String code;
    private LocalDateTime createdAt;
}