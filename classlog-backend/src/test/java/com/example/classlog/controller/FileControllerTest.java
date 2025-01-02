package com.example.classlog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.FileDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.service.FileService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


class FileControllerTest {

  @Mock
  private FileService fileService;

  @InjectMocks
  private FileController fileController;

  private FileDto fileDto;

  private final String testFilePath = "src/test/testUploads/julia_set_with_axes.png";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    UserDto userDto = UserDto.builder()
        .id(1L)
        .email("user@example.com")
        .build();

    ClassDto classDto = ClassDto.builder()
        .id(1L)
        .name("Test Class")
        .description("Sample Class Description")
        .build();

    fileDto = FileDto.builder()
        .fileId(1L)
        .filePath(testFilePath)
        .assignedClass(classDto)
        .uploadedBy(userDto)
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Test
  void uploadFile() throws IOException {
    // Given
    MultipartFile mockFile = new MockMultipartFile(
        "file",
        "julia_set_with_axes.png",
        "image/png",
        "dummy content".getBytes()
    );

    Mockito.when(fileService.saveFile(any(FileDto.class), any(MultipartFile.class)))
        .thenReturn(fileDto);

    // When
    ResponseEntity<FileDto> response = fileController.uploadFile(mockFile, fileDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(fileDto.getFileId(), Objects.requireNonNull(response.getBody()).getFileId());
    assertEquals(fileDto.getFilePath(), response.getBody().getFilePath());
    assertEquals(fileDto.getAssignedClass().getName(),
        response.getBody().getAssignedClass().getName());
    assertEquals(fileDto.getUploadedBy().getEmail(), response.getBody().getUploadedBy().getEmail());
  }

  @Test
  void getFilesByClassId() {
    // Given
    List<FileDto> files = Arrays.asList(fileDto, fileDto);
    Mockito.when(fileService.getFilesByClassId(1L)).thenReturn(files);

    // When
    List<FileDto> response = fileController.getFilesByClassId(1L);

    // Then
    assertEquals(2, response.size());
    assertEquals(fileDto.getFilePath(), response.get(0).getFilePath());
  }

  @Test
  void getFileById() {
    // Given
    Mockito.when(fileService.getFileById(1L)).thenReturn(fileDto);

    // When
    FileDto response = fileController.getFileById(1L);

    // Then
    assertEquals(fileDto.getFileId(), response.getFileId());
    assertEquals(fileDto.getFilePath(), response.getFilePath());
    assertEquals(fileDto.getAssignedClass().getName(), response.getAssignedClass().getName());
    assertEquals(fileDto.getUploadedBy().getEmail(), response.getUploadedBy().getEmail());
  }

  @Test
  void deleteFile() {
    // Given
    Mockito.doNothing().when(fileService).deleteFileById(any(FileDto.class));

    // When
    ResponseEntity<String> response = fileController.deleteFile(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("File deleted successfully.", response.getBody());
  }

  @Test
  void downloadFile() throws IOException {
    // Given
    Path filePath = Path.of(testFilePath);

    // Ensure the file exists but don't create it if it already exists
    if (!Files.exists(filePath)) {
      Files.createDirectories(filePath.getParent()); // Ensure the directory exists
      Files.createFile(filePath); // Create the test file
    }

    Resource resource = new UrlResource(filePath.toUri());

    Mockito.when(fileService.getFileById(1L)).thenReturn(fileDto);

    // When
    ResponseEntity<Resource> response = fileController.downloadFile(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertNotNull(response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION));
    assertEquals("attachment; filename=\"julia_set_with_axes.png\"",
        response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));

    // Clean up the test file only if you created it
    if (Files.exists(filePath)) {
      Files.deleteIfExists(filePath);
    }
  }


  @Test
  void getFilesByClassIdEmpty() {
    // Given
    Mockito.when(fileService.getFilesByClassId(1L)).thenReturn(Collections.emptyList());

    // When
    List<FileDto> response = fileController.getFilesByClassId(1L);

    // Then
    assertEquals(0, response.size());
  }

  @Test
  void getFileByIdNotFound() {
    // Given
    Mockito.when(fileService.getFileById(2L)).thenReturn(null);

    // When
    FileDto response = fileController.getFileById(2L);

    // Then
    assertNull(response);
  }
}
