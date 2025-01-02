package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.FileDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entity.Class;
import com.example.classlog.entity.File;
import com.example.classlog.entity.User;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FileMapperTest {

  private FileMapperImpl fileMapper;

  @Mock
  private ClassMapper classMapper;

  @Mock
  private UserMapper userMapper;

  private File file;
  private FileDto fileDto;
  private Class classEntity;
  private ClassDto classDto;
  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // Manually instantiate the FileMapper and inject ClassMapper and UserMapper
    fileMapper = new FileMapperImpl();

    Field classMapperField = FileMapperImpl.class.getDeclaredField("classMapper");
    classMapperField.setAccessible(true);
    classMapperField.set(fileMapper, classMapper);

    Field userMapperField = FileMapperImpl.class.getDeclaredField("userMapper");
    userMapperField.setAccessible(true);
    userMapperField.set(fileMapper, userMapper);

    // Initialize mock objects
    classEntity = Class.builder()
        .id(1L)
        .name("Test Class")
        .description("A test class description")
        .code("TEST123")
        .createdAt(LocalDateTime.now())
        .build();

    classDto = ClassDto.builder()
        .id(1L)
        .name("Test Class")
        .description("A test class description")
        .code("TEST123")
        .createdAt(LocalDateTime.now())
        .build();

    user = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();

    userDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    file = File.builder()
        .fileId(1L)
        .filePath("files\\document.pdf")
        .classEntity(classEntity)
        .userEntity(user)
        .createdAt(LocalDateTime.now())
        .build();

    fileDto = FileDto.builder()
        .fileId(1L)
        .filePath("files/document.pdf")
        .assignedClass(classDto)
        .uploadedBy(userDto)
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Test
  void shouldMapFileToFileDto() {
    // Given
    when(classMapper.toClassDto(classEntity)).thenReturn(classDto);
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    FileDto mappedDto = fileMapper.toFileDto(file);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getFileId()).isEqualTo(file.getFileId());
    assertThat(mappedDto.getFilePath()).isEqualTo("files/document.pdf"); // Verify normalized path
    assertThat(mappedDto.getAssignedClass()).isEqualTo(classDto);
    assertThat(mappedDto.getUploadedBy()).isEqualTo(userDto);
    assertThat(mappedDto.getCreatedAt()).isEqualTo(file.getCreatedAt());
  }

  @Test
  void shouldMapFileDtoToFile() {
    // Given
    when(classMapper.toEntity(classDto)).thenReturn(classEntity);
    when(userMapper.toUser(userDto)).thenReturn(user);

    // When
    File mappedEntity = fileMapper.toEntity(fileDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getFileId()).isEqualTo(fileDto.getFileId());
    assertThat(mappedEntity.getFilePath()).isEqualTo(
        "files/document.pdf"); // Verify normalized path
    assertThat(mappedEntity.getClassEntity()).isEqualTo(classEntity);
    assertThat(mappedEntity.getUserEntity()).isEqualTo(user);
    assertThat(mappedEntity.getCreatedAt()).isEqualTo(fileDto.getCreatedAt());
  }

  @Test
  void shouldHandleNullValuesForEntityToDto() {
    // When
    FileDto mappedDto = fileMapper.toFileDto(null);

    // Then
    assertThat(mappedDto).isNull();
  }

  @Test
  void shouldHandleNullValuesForDtoToEntity() {
    // When
    File mappedEntity = fileMapper.toEntity(null);

    // Then
    assertThat(mappedEntity).isNull();
  }

  @Test
  void shouldHandlePartialNullValuesForEntityToDto() {
    // Given
    File partialFile = File.builder()
        .fileId(2L)
        .filePath(null)
        .classEntity(null)
        .userEntity(null)
        .createdAt(LocalDateTime.now())
        .build();

    // When
    FileDto mappedDto = fileMapper.toFileDto(partialFile);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getFileId()).isEqualTo(2L);
    assertThat(mappedDto.getFilePath()).isNull();
    assertThat(mappedDto.getAssignedClass()).isNull();
    assertThat(mappedDto.getUploadedBy()).isNull();
    assertThat(mappedDto.getCreatedAt()).isEqualTo(partialFile.getCreatedAt());
  }

  @Test
  void shouldHandlePartialNullValuesForDtoToEntity() {
    // Given
    FileDto partialFileDto = FileDto.builder()
        .fileId(2L)
        .filePath(null)
        .assignedClass(null)
        .uploadedBy(null)
        .createdAt(LocalDateTime.now())
        .build();

    // When
    File mappedEntity = fileMapper.toEntity(partialFileDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getFileId()).isEqualTo(2L);
    assertThat(mappedEntity.getFilePath()).isNull();
    assertThat(mappedEntity.getClassEntity()).isNull();
    assertThat(mappedEntity.getUserEntity()).isNull();
    assertThat(mappedEntity.getCreatedAt()).isEqualTo(partialFileDto.getCreatedAt());
  }
}
