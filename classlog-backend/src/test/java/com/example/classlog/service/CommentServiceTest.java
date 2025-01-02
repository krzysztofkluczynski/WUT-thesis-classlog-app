package com.example.classlog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.dto.CommentDto;
import com.example.classlog.entity.Comment;
import com.example.classlog.entity.Post;
import com.example.classlog.entity.User;
import com.example.classlog.mapper.CommentMapper;
import com.example.classlog.repository.CommentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CommentServiceTest {

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private CommentMapper commentMapper;

  @InjectMocks
  private CommentService commentService;

  private Comment comment;
  private CommentDto commentDto;
  private Post post;
  private User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Initialize test data
    post = Post.builder()
        .id(1L)
        .title("Sample Post")
        .content("Post content")
        .build();

    user = User.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    comment = Comment.builder()
        .id(1L)
        .post(post)
        .user(user)
        .content("Sample comment")
        .createdAt(LocalDateTime.now())
        .build();

    commentDto = CommentDto.builder()
        .id(1L)
        .post(null) // Assuming post DTO mapping is handled elsewhere
        .user(null) // Assuming user DTO mapping is handled elsewhere
        .content("Sample comment")
        .createdAt(comment.getCreatedAt())
        .build();
  }

  @Test
  void shouldFindAllComments() {
    // Given
    when(commentRepository.findAll()).thenReturn(List.of(comment));
    when(commentMapper.toCommentDto(comment)).thenReturn(commentDto);

    // When
    List<CommentDto> result = commentService.findAllComments();

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getContent()).isEqualTo("Sample comment");
    verify(commentRepository, times(1)).findAll();
    verify(commentMapper, times(1)).toCommentDto(comment);
  }

  @Test
  void shouldReturnEmptyListWhenNoCommentsFound() {
    // Given
    when(commentRepository.findAll()).thenReturn(List.of());

    // When
    List<CommentDto> result = commentService.findAllComments();

    // Then
    assertThat(result).isEmpty();
    verify(commentRepository, times(1)).findAll();
  }

  @Test
  void shouldFindCommentById() {
    // Given
    when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
    when(commentMapper.toCommentDto(comment)).thenReturn(commentDto);

    // When
    CommentDto result = commentService.findCommentById(1L);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getContent()).isEqualTo("Sample comment");
    verify(commentRepository, times(1)).findById(1L);
    verify(commentMapper, times(1)).toCommentDto(comment);
  }

  @Test
  void shouldReturnNullWhenCommentNotFoundById() {
    // Given
    when(commentRepository.findById(1L)).thenReturn(Optional.empty());

    // When
    CommentDto result = commentService.findCommentById(1L);

    // Then
    assertThat(result).isNull();
    verify(commentRepository, times(1)).findById(1L);
    verify(commentMapper, times(0)).toCommentDto(any());
  }

  @Test
  void shouldFindCommentsByPostId() {
    // Given
    when(commentRepository.findByPost_Id(1L)).thenReturn(List.of(comment));
    when(commentMapper.toCommentDto(comment)).thenReturn(commentDto);

    // When
    List<CommentDto> result = commentService.findCommentsByPostId(1L);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getContent()).isEqualTo("Sample comment");
    verify(commentRepository, times(1)).findByPost_Id(1L);
    verify(commentMapper, times(1)).toCommentDto(comment);
  }

  @Test
  void shouldReturnEmptyListWhenNoCommentsFoundForPost() {
    // Given
    when(commentRepository.findByPost_Id(1L)).thenReturn(List.of());

    // When
    List<CommentDto> result = commentService.findCommentsByPostId(1L);

    // Then
    assertThat(result).isEmpty();
    verify(commentRepository, times(1)).findByPost_Id(1L);
  }

  @Test
  void shouldCreateComment() {
    // Given
    when(commentMapper.toEntity(commentDto)).thenReturn(comment);
    when(commentRepository.save(comment)).thenReturn(comment);
    when(commentMapper.toCommentDto(comment)).thenReturn(commentDto);

    // When
    CommentDto result = commentService.createComment(commentDto);

    // Then
    assertThat(result).isNotNull();

    ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
    verify(commentRepository, times(1)).save(captor.capture());
    assertThat(captor.getValue().getContent()).isEqualTo("Sample comment");
  }

  @Test
  void shouldThrowExceptionWhenSavingNullComment() {
    // When & Then
    assertThatThrownBy(() -> commentService.createComment(null))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("Comment cannot be null");
  }


  @Test
  void shouldDeleteComment() {
    // When
    commentService.deleteComment(1L);

    // Then
    verify(commentRepository, times(1)).deleteById(1L);
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistingComment() {
    // Given
    doThrow(new IllegalArgumentException("Comment not found")).when(commentRepository)
        .deleteById(1L);

    // When & Then
    assertThatThrownBy(() -> commentService.deleteComment(1L))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Comment not found");
  }
}
