package com.example.classlog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.PostDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.service.PostService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class PostControllerTest {

  @Mock
  private PostService postService;

  @InjectMocks
  private PostController postController;

  private PostDto postDto;

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

    postDto = PostDto.builder()
        .id(1L)
        .assignedClass(classDto)
        .user(userDto)
        .title("Sample Post Title")
        .content("Sample Post Content")
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Test
  void getAllPosts() {
    // Given
    List<PostDto> posts = Arrays.asList(postDto, postDto);
    Mockito.when(postService.findAllPosts()).thenReturn(posts);

    // When
    ResponseEntity<List<PostDto>> response = postController.getAllPosts();

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
  }

  @Test
  void getPostById() {
    // Given
    Mockito.when(postService.findPostById(1L)).thenReturn(postDto);

    // When
    ResponseEntity<PostDto> response = postController.getPostById(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(postDto.getId(), response.getBody().getId());
  }

  @Test
  void getPostByClassId() {
    // Given
    List<PostDto> posts = Arrays.asList(postDto, postDto);
    Mockito.when(postService.findPostByClassId(1L)).thenReturn(posts);

    // When
    ResponseEntity<List<PostDto>> response = postController.getPostByClassId(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
  }

  @Test
  void createPost() {
    // Given
    Mockito.when(postService.createPost(any(PostDto.class))).thenReturn(postDto);

    // When
    ResponseEntity<PostDto> response = postController.createPost(postDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(postDto.getId(), response.getBody().getId());
  }

  @Test
  void deletePost() {
    // When
    ResponseEntity<Void> response = postController.deletePost(1L);

    // Then
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  void getAllPostsEmpty() {
    // Given
    Mockito.when(postService.findAllPosts()).thenReturn(Collections.emptyList());

    // When
    ResponseEntity<List<PostDto>> response = postController.getAllPosts();

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, response.getBody().size());
  }

  @Test
  void getPostByClassIdEmpty() {
    // Given
    Mockito.when(postService.findPostByClassId(1L)).thenReturn(Collections.emptyList());

    // When
    ResponseEntity<List<PostDto>> response = postController.getPostByClassId(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, response.getBody().size());
  }
}
