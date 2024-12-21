package com.example.classlog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.CreateClassDto;
import com.example.classlog.dto.ManageUserClassRequestDto;
import com.example.classlog.dto.ManageUserClassWithCodeDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.service.ClassService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class ClassControllerTest {

  @Mock
  private ClassService classService;

  @InjectMocks
  private ClassController classController;

  private ClassDto classDto;
  private CreateClassDto createClassDto;
  private ManageUserClassRequestDto manageUserClassRequestDto;
  private ManageUserClassWithCodeDto manageUserClassWithCodeDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    classDto = ClassDto.builder()
        .id(1L)
        .name("Sample Class")
        .description("A sample class description")
        .code("CODE123")
        .createdAt(LocalDateTime.now())
        .build();

    createClassDto = CreateClassDto.builder()
        .createdBy(UserDto.builder().id(1L).email("user@example.com").build())
        .classDto(classDto)
        .build();

    manageUserClassRequestDto = ManageUserClassRequestDto.builder()
        .classId(1L)
        .users(Arrays.asList(
            UserDto.builder().id(1L).email("user1@example.com").build(),
            UserDto.builder().id(2L).email("user2@example.com").build()
        ))
        .build();

    manageUserClassWithCodeDto = ManageUserClassWithCodeDto.builder()
        .classCode("CODE123")
        .user(UserDto.builder().id(1L).email("user@example.com").build())
        .build();
  }

  @Test
  void getClassById() {
    // Given
    Mockito.when(classService.findClassById(1L)).thenReturn(classDto);

    // When
    ResponseEntity<ClassDto> response = classController.getClassById(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(classDto.getId(), Objects.requireNonNull(response.getBody()).getId());
  }

  @Test
  void getClassesByUserId() {
    // Given
    List<ClassDto> classes = Arrays.asList(classDto, classDto);
    Mockito.when(classService.findClassesByUserId(1L)).thenReturn(classes);

    // When
    ResponseEntity<List<ClassDto>> response = classController.getClassesByUserId(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(classes.size(), Objects.requireNonNull(response.getBody()).size());
  }

  @Test
  void addClass() {
    // Given
    Mockito.when(classService.addClass(any(CreateClassDto.class))).thenReturn(classDto);

    // When
    ResponseEntity<ClassDto> response = classController.addClass(createClassDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(classDto.getId(), Objects.requireNonNull(response.getBody()).getId());
  }

  @Test
  void deleteUsersFromClass() {
    // When
    ResponseEntity<String> response = classController.deleteUsersFromClass(
        manageUserClassRequestDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Users successfully removed from the class.", response.getBody());
  }

  @Test
  void addUsersToClass() {
    // When
    ResponseEntity<String> response = classController.addUsersToClass(manageUserClassRequestDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Users successfully added to the class.", response.getBody());
  }

  @Test
  void addUsersToClassByCode() {
    // When
    ResponseEntity<String> response = classController.addUsersToClass(manageUserClassWithCodeDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Users successfully added to the class.", response.getBody());
  }

  @Test
  void getEmptyClassById() {
    // Given
    Mockito.when(classService.findClassById(2L)).thenReturn(null);

    // When
    ResponseEntity<ClassDto> response = classController.getClassById(2L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(null, response.getBody());
  }

  @Test
  void getEmptyClassesByUserId() {
    // Given
    Mockito.when(classService.findClassesByUserId(2L)).thenReturn(Collections.emptyList());

    // When
    ResponseEntity<List<ClassDto>> response = classController.getClassesByUserId(2L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, response.getBody().size());
  }
}
