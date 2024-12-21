package com.example.classlog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.classlog.entities.Class;
import com.example.classlog.entities.File;
import com.example.classlog.entities.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class FileRepositoryTest {

  @Autowired
  private FileRepository fileRepository;

  @Autowired
  private ClassRepository classRepository;

  @Autowired
  private UserRepository userRepository;

  private Class classEntity;
  private User user;

  @BeforeEach
  void setUp() {
    // Given: Prepare test data for Class and User
    classEntity = classRepository.save(
        Class.builder()
            .name("Test Class")
            .description("Class Description")
            .code("CLASS2024")
            .build()
    );

    user = userRepository.save(
        User.builder()
            .name("John")
            .surname("Doe")
            .email("user@example.com")
            .password("password123")
            .build()
    );

    // Given: Save a File entity associated with Class and User
    fileRepository.save(
        File.builder()
            .classEntity(classEntity)
            .userEntity(user)
            .filePath("path/to/file")
            .build()
    );
  }

  @Test
  void testFindByClassId() {
    // Given
    Long classId = classEntity.getId();

    // When
    List<File> files = fileRepository.findByClassEntityClass_Id(classId);

    // Then
    assertNotNull(files);
    assertEquals(1, files.size());
    assertEquals("path/to/file", files.get(0).getFilePath());
  }

  @Test
  void testFindById() {
    // Given
    File savedFile = fileRepository.findAll().get(0); // Get saved file
    Long fileId = savedFile.getFileId();

    // When
    Optional<File> file = fileRepository.findById(fileId);

    // Then
    assertTrue(file.isPresent());
    assertEquals("path/to/file", file.get().getFilePath());
  }
}
