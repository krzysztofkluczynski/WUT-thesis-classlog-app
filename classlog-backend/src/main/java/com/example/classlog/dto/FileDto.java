package com.example.classlog.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDto {

    private Long fileId;
    private String filePath;
    private ClassDto assignedClass;
    private UserDto uploadedBy;
    private LocalDateTime createdAt;
}