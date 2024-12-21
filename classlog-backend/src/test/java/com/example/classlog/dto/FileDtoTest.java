package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class FileDtoTest {

  @Test
  void shouldCreateFileDtoUsingBuilder() {
    // Given
    Long fileId = 1L;
    String filePath = "path/to/file";
    ClassDto assignedClass = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto uploadedBy = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    LocalDateTime createdAt = LocalDateTime.now();

    // When
    FileDto fileDto = FileDto.builder()
        .fileId(fileId)
        .filePath(filePath)
        .assignedClass(assignedClass)
        .uploadedBy(uploadedBy)
        .createdAt(createdAt)
        .build();

    // Then
    assertThat(fileDto.getFileId()).isEqualTo(fileId);
    assertThat(fileDto.getFilePath()).isEqualTo(filePath);
    assertThat(fileDto.getAssignedClass().getId()).isEqualTo(
        assignedClass.getId());  // Compare only core fields
    assertThat(fileDto.getUploadedBy().getId()).isEqualTo(
        uploadedBy.getId());  // Compare only core fields
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    FileDto fileDto = FileDto.builder().build();

    // When
    Long fileId = 1L;
    String filePath = "path/to/file";
    ClassDto assignedClass = ClassDto.builder()
        .id(1L)
        .name("Math 101")
        .description("Introduction to Mathematics")
        .code("MATH101")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto uploadedBy = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    LocalDateTime createdAt = LocalDateTime.now();

    fileDto.setFileId(fileId);
    fileDto.setFilePath(filePath);
    fileDto.setAssignedClass(assignedClass);
    fileDto.setUploadedBy(uploadedBy);
    fileDto.setCreatedAt(createdAt);

    // Then
    assertThat(fileDto.getFileId()).isEqualTo(fileId);
    assertThat(fileDto.getFilePath()).isEqualTo(filePath);
    assertThat(fileDto.getAssignedClass().getId()).isEqualTo(
        assignedClass.getId());  // Compare only core fields
    assertThat(fileDto.getUploadedBy().getId()).isEqualTo(
        uploadedBy.getId());  // Compare only core fields
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    FileDto fileDto = FileDto.builder().build();

    // When & Then
    assertThat(fileDto.getFileId()).isNull();
    assertThat(fileDto.getFilePath()).isNull();
    assertThat(fileDto.getAssignedClass()).isNull();
    assertThat(fileDto.getUploadedBy()).isNull();
    assertThat(fileDto.getCreatedAt()).isNull();
  }
}
