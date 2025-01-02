package com.example.classlog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.dto.FileDto;
import com.example.classlog.entity.Class;
import com.example.classlog.entity.File;
import com.example.classlog.entity.User;
import com.example.classlog.mapper.FileMapper;
import com.example.classlog.repository.FileRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FileServiceTest {

  @Mock
  private FileRepository fileRepository;

  @Mock
  private FileMapper fileMapper;

  @InjectMocks
  private FileService fileService;

  private File file;
  private FileDto fileDto;
  private Class assignedClass;
  private User uploadedBy;

  private final String mockFilePath = "src/test/testUploads/julia_set_with_axes.png";
  private ArgumentCaptor<File> fileCaptor;
  private ArgumentCaptor<Long> idCaptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    assignedClass = Class.builder()
        .id(1L)
        .name("Test Class")
        .build();

    uploadedBy = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    file = File.builder()
        .fileId(1L)
        .filePath(mockFilePath.replace("\\", "/"))
        .classEntity(assignedClass)
        .userEntity(uploadedBy)
        .build();

    fileDto = FileDto.builder()
        .fileId(1L)
        .filePath(mockFilePath.replace("\\", "/"))
        .assignedClass(null)
        .uploadedBy(null)
        .build();

    fileCaptor = ArgumentCaptor.forClass(File.class);
    idCaptor = ArgumentCaptor.forClass(Long.class);
  }

  @Test
  void shouldGetFilesByClassId() {
    // Given
    when(fileRepository.findByClassEntityClass_Id(1L)).thenReturn(List.of(file));
    when(fileMapper.toFileDto(file)).thenReturn(fileDto);

    // When
    List<FileDto> result = fileService.getFilesByClassId(1L);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getFilePath()).isEqualTo(mockFilePath.replace("\\", "/"));
    verify(fileRepository, times(1)).findByClassEntityClass_Id(1L);
  }

  @Test
  void shouldThrowExceptionWhenFileNotFoundById() {
    // Given
    when(fileRepository.findById(1L)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> fileService.getFileById(1L))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("File not found");
    verify(fileRepository, times(1)).findById(1L);
  }

  @Test
  void shouldSaveFileWithoutMultipart() {
    // Given
    when(fileMapper.toEntity(fileDto)).thenReturn(file);
    when(fileRepository.save(file)).thenReturn(file);
    when(fileMapper.toFileDto(file)).thenReturn(fileDto);

    // When
    FileDto result = fileService.saveFile(fileDto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getFilePath()).isEqualTo(mockFilePath.replace("\\", "/"));
    verify(fileRepository, times(1)).save(fileCaptor.capture());

    File capturedFile = fileCaptor.getValue();
    assertThat(capturedFile.getFilePath()).isEqualTo(mockFilePath.replace("\\", "/"));
  }

  @Test
  void shouldDeleteFileById() throws IOException {
    // Given: Create a temporary file to simulate the file on the filesystem
    Path tempFile = Files.createTempFile("test_file", ".png");
    File tempFileEntity = File.builder()
        .fileId(1L)
        .filePath(tempFile.toString())
        .classEntity(assignedClass)
        .userEntity(uploadedBy)
        .build();

    FileDto tempFileDto = FileDto.builder()
        .fileId(1L)
        .filePath(tempFile.toString())
        .assignedClass(null)
        .uploadedBy(null)
        .build();

    when(fileRepository.findById(1L)).thenReturn(Optional.of(tempFileEntity));

    // Verify that the file exists before deletion
    assertThat(Files.exists(tempFile)).isTrue();

    // When: Call the deleteFileById method
    fileService.deleteFileById(tempFileDto);

    // Then: Verify that the file is deleted and the repository method is called
    assertThat(Files.exists(tempFile)).isFalse();
    verify(fileRepository, times(1)).deleteById(idCaptor.capture());

    Long capturedId = idCaptor.getValue();
    assertThat(capturedId).isEqualTo(1L);
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistingFile() {
    // Given
    when(fileRepository.findById(1L)).thenReturn(Optional.of(file));

    file.setFilePath("non/existing/path.png");

    // When & Then
    assertThatThrownBy(() -> fileService.deleteFileById(fileDto))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("File not found on the filesystem");
  }
}
