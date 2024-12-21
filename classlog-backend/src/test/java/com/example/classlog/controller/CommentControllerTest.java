package com.example.classlog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.example.classlog.dto.CommentDto;
import com.example.classlog.dto.PostDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.service.CommentService;
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


class CommentControllerTest {

  @Mock
  private CommentService commentService;

  @InjectMocks
  private CommentController commentController;

  private CommentDto commentDto;
  private PostDto postDto;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    userDto = UserDto.builder()
        .id(1L)
        .email("user@example.com")
        .build();

    postDto = PostDto.builder()
        .id(1L)
        .title("Sample Post")
        .content("This is a sample post content")
        .createdAt(LocalDateTime.now())
        .build();

    commentDto = CommentDto.builder()
        .id(1L)
        .post(postDto)
        .user(userDto)
        .content("Sample comment content")
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Test
  void getAllComments() {
    // Given
    List<CommentDto> comments = Arrays.asList(commentDto, commentDto);
    Mockito.when(commentService.findAllComments()).thenReturn(comments);

    // When
    ResponseEntity<List<CommentDto>> response = commentController.getAllComments();

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(comments.size(), response.getBody().size());
  }

  @Test
  void getCommentById() {
    // Given
    Mockito.when(commentService.findCommentById(1L)).thenReturn(commentDto);

    // When
    ResponseEntity<CommentDto> response = commentController.getCommentById(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(commentDto.getId(), response.getBody().getId());
  }

  @Test
  void getCommentsByPostId() {
    // Given
    List<CommentDto> comments = Arrays.asList(commentDto, commentDto);
    Mockito.when(commentService.findCommentsByPostId(1L)).thenReturn(comments);

    // When
    ResponseEntity<List<CommentDto>> response = commentController.getCommentsByPostId(1L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(comments.size(), response.getBody().size());
  }

  @Test
  void createComment() {
    // Given
    Mockito.when(commentService.createComment(any(CommentDto.class))).thenReturn(commentDto);

    // When
    ResponseEntity<CommentDto> response = commentController.createPost(commentDto);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(commentDto.getId(), response.getBody().getId());
  }

  @Test
  void deleteComment() {
    // When
    ResponseEntity<Void> response = commentController.deleteComment(1L);

    // Then
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  void getEmptyComments() {
    // Given
    Mockito.when(commentService.findAllComments()).thenReturn(Collections.emptyList());

    // When
    ResponseEntity<List<CommentDto>> response = commentController.getAllComments();

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, response.getBody().size());
  }

  @Test
  void getEmptyCommentById() {
    // Given
    Mockito.when(commentService.findCommentById(2L)).thenReturn(null);

    // When
    ResponseEntity<CommentDto> response = commentController.getCommentById(2L);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(null, response.getBody());
  }
}
