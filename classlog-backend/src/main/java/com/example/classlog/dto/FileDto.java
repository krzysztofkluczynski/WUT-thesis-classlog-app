package com.example.classlog.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileDto {
    private Long fileId;
    private String filePath;
    private ClassDto assignedClass;   // Full Class object
    private UserDto uploadedBy;       // Full User object for the uploader
    private LocalDateTime createdAt;
}