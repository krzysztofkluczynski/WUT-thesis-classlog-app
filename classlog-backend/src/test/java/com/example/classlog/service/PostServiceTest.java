package com.example.classlog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.PostDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entity.Class;
import com.example.classlog.entity.Post;
import com.example.classlog.entity.User;
import com.example.classlog.mapper.PostMapper;
import com.example.classlog.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PostServiceTest {

  @Mock
  private PostRepository postRepository;

  @Mock
  private PostMapper postMapper;

  @InjectMocks
  private PostService postService;

  private Post post;
  private PostDto postDto;
  private Class assignedClass;
  private User user;

  private ArgumentCaptor<Post> postCaptor;
  private ArgumentCaptor<Long> idCaptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    assignedClass = Class.builder()
        .id(1L)
        .name("Test Class")
        .build();

    user = User.builder()
        .id(2L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .build();

    post = Post.builder()
        .id(1L)
        .assignedClass(assignedClass)
        .user(user)
        .title("Test Post")
        .content("This is a test post content")
        .createdAt(LocalDateTime.now())
        .build();

    postDto = PostDto.builder()
        .id(1L)
        .assignedClass(ClassDto.builder().id(1L).build())
        .user(UserDto.builder().id(2L).build())
        .title("Test Post")
        .content("This is a test post content")
        .createdAt(LocalDateTime.now())
        .build();

    postCaptor = ArgumentCaptor.forClass(Post.class);
    idCaptor = ArgumentCaptor.forClass(Long.class);
  }

  @Test
  void shouldFindAllPosts() {
    // Given
    when(postRepository.findAll()).thenReturn(List.of(post));
    when(postMapper.toPostDto(post)).thenReturn(postDto);

    // When
    List<PostDto> result = postService.findAllPosts();

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTitle()).isEqualTo("Test Post");
    verify(postRepository, times(1)).findAll();
  }

  @Test
  void shouldReturnEmptyListWhenNoPostsFound() {
    // Given
    when(postRepository.findAll()).thenReturn(Collections.emptyList());

    // When
    List<PostDto> result = postService.findAllPosts();

    // Then
    assertThat(result).isEmpty();
    verify(postRepository, times(1)).findAll();
  }

  @Test
  void shouldFindPostById() {
    // Given
    when(postRepository.findById(1L)).thenReturn(Optional.of(post));
    when(postMapper.toPostDto(post)).thenReturn(postDto);

    // When
    PostDto result = postService.findPostById(1L);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isEqualTo("Test Post");
    verify(postRepository, times(1)).findById(idCaptor.capture());
    assertThat(idCaptor.getValue()).isEqualTo(1L);
  }

  @Test
  void shouldReturnNullWhenPostByIdNotFound() {
    // Given
    when(postRepository.findById(1L)).thenReturn(Optional.empty());

    // When
    PostDto result = postService.findPostById(1L);

    // Then
    assertThat(result).isNull();
    verify(postRepository, times(1)).findById(idCaptor.capture());
    assertThat(idCaptor.getValue()).isEqualTo(1L);
  }

  @Test
  void shouldFindPostByClassId() {
    // Given
    when(postRepository.findByAssignedClass_Id(1L)).thenReturn(List.of(post));
    when(postMapper.toPostDto(post)).thenReturn(postDto);

    // When
    List<PostDto> result = postService.findPostByClassId(1L);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTitle()).isEqualTo("Test Post");
    verify(postRepository, times(1)).findByAssignedClass_Id(1L);
  }

  @Test
  void shouldReturnEmptyListWhenNoPostsByClassId() {
    // Given
    when(postRepository.findByAssignedClass_Id(1L)).thenReturn(Collections.emptyList());

    // When
    List<PostDto> result = postService.findPostByClassId(1L);

    // Then
    assertThat(result).isEmpty();
    verify(postRepository, times(1)).findByAssignedClass_Id(1L);
  }

  @Test
  void shouldCreatePost() {
    // Given
    when(postMapper.toEntity(postDto)).thenReturn(post);
    when(postRepository.save(post)).thenReturn(post);
    when(postMapper.toPostDto(post)).thenReturn(postDto);

    // When
    PostDto result = postService.createPost(postDto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isEqualTo("Test Post");
    verify(postRepository, times(1)).save(postCaptor.capture());

    Post capturedPost = postCaptor.getValue();
    assertThat(capturedPost.getTitle()).isEqualTo("Test Post");
    assertThat(capturedPost.getContent()).isEqualTo("This is a test post content");
  }

  @Test
  void shouldDeletePost() {
    // When
    postService.deletePost(1L);

    // Then
    verify(postRepository, times(1)).deleteById(idCaptor.capture());
    assertThat(idCaptor.getValue()).isEqualTo(1L);
  }
}
