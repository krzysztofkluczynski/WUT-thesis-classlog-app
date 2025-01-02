package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.entity.Class;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ClassMapperTest {

  private ClassMapper classMapper;

  private Class classEntity;
  private ClassDto classDto;

  @BeforeEach
  void setUp() {
    classMapper = Mappers.getMapper(ClassMapper.class);

    classEntity = Class.builder()
        .id(1L)
        .name("Test Class")
        .description("Description of the test class")
        .code("TEST123")
        .createdAt(LocalDateTime.now())
        .build();

    classDto = ClassDto.builder()
        .id(1L)
        .name("Test Class")
        .description("Description of the test class")
        .code("TEST123")
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Test
  void shouldMapEntityToDto() {
    // When
    ClassDto result = classMapper.toClassDto(classEntity);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(classEntity.getId());
    assertThat(result.getName()).isEqualTo(classEntity.getName());
    assertThat(result.getDescription()).isEqualTo(classEntity.getDescription());
    assertThat(result.getCode()).isEqualTo(classEntity.getCode());
    assertThat(result.getCreatedAt()).isEqualTo(classEntity.getCreatedAt());
  }

  @Test
  void shouldMapDtoToEntity() {
    // When
    Class result = classMapper.toEntity(classDto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(classDto.getId());
    assertThat(result.getName()).isEqualTo(classDto.getName());
    assertThat(result.getDescription()).isEqualTo(classDto.getDescription());
    assertThat(result.getCode()).isEqualTo(classDto.getCode());
    assertThat(result.getCreatedAt()).isEqualTo(classDto.getCreatedAt());
  }

  @Test
  void shouldTestClassEntityMethods() {
    // When
    classEntity.setId(2L);
    classEntity.setName("Updated Class");
    classEntity.setDescription("Updated description");
    classEntity.setCode("UPDATED123");
    classEntity.setCreatedAt(LocalDateTime.now().minusDays(1));

    // Then
    assertThat(classEntity.getId()).isEqualTo(2L);
    assertThat(classEntity.getName()).isEqualTo("Updated Class");
    assertThat(classEntity.getDescription()).isEqualTo("Updated description");
    assertThat(classEntity.getCode()).isEqualTo("UPDATED123");
    assertThat(classEntity.getCreatedAt()).isBefore(LocalDateTime.now());
  }

  @Test
  void shouldTestClassDtoMethods() {
    // When
    classDto.setId(2L);
    classDto.setName("Updated Class");
    classDto.setDescription("Updated description");
    classDto.setCode("UPDATED123");
    classDto.setCreatedAt(LocalDateTime.now().minusDays(1));

    // Then
    assertThat(classDto.getId()).isEqualTo(2L);
    assertThat(classDto.getName()).isEqualTo("Updated Class");
    assertThat(classDto.getDescription()).isEqualTo("Updated description");
    assertThat(classDto.getCode()).isEqualTo("UPDATED123");
    assertThat(classDto.getCreatedAt()).isBefore(LocalDateTime.now());
  }
}
