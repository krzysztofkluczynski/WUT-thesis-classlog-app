package com.example.classlog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.classlog.entities.Class;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ClassRepositoryTest {

  @Autowired
  private ClassRepository classRepository;

  @BeforeEach
  void setUp() {
    // Prepare test data
    Class testClass = Class.builder()
        .name("Mathematics")
        .description("Advanced mathematics class")
        .code("MATH2024")
        .build();
    classRepository.save(testClass);
  }

  @Test
  void testFindByCode() {
    // Act
    Optional<Class> result = classRepository.findByCode("MATH2024");

    // Assert
    assertTrue(result.isPresent());
    assertEquals("Mathematics", result.get().getName());
    assertEquals("MATH2024", result.get().getCode());
  }

  @Test
  void testFindByUserId() {
    // Act
    List<Class> classes = classRepository.findByUserId(1L); // Replace with appropriate userId

    // Assert
    assertNotNull(classes);
    assertEquals(0, classes.size()); // Adjust based on your test data and relationships
  }

  @Test
  void testPrePersistAnnotation() {
    // Given
    Class newClass = Class.builder()
        .name("Physics")
        .description("Physics basics")
        .code("PHYS2024")
        .build();

    // Act
    Class savedClass = classRepository.save(newClass);

    // Assert
    assertNotNull(savedClass.getCreatedAt()); // Check @PrePersist behavior
    assertEquals("Physics", savedClass.getName());
  }
}
