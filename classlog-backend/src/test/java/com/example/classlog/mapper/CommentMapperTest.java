package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.CommentDto;
import com.example.classlog.dto.PostDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.Comment;
import com.example.classlog.entities.Post;
import com.example.classlog.entities.User;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CommentMapperTest {

  private CommentMapperImpl commentMapper;

  @Mock
  private PostMapper postMapper;

  @Mock
  private UserMapper userMapper;

  private Comment comment;
  private CommentDto commentDto;
  private Post post;
  private PostDto postDto;
  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // Manually instantiate the mapper
    commentMapper = new CommentMapperImpl();

    // Inject dependencies
    Field postMapperField = CommentMapperImpl.class.getDeclaredField("postMapper");
    postMapperField.setAccessible(true);
    postMapperField.set(commentMapper, postMapper);

    Field userMapperField = CommentMapperImpl.class.getDeclaredField("userMapper");
    userMapperField.setAccessible(true);
    userMapperField.set(commentMapper, userMapper);

    // Initialize objects
    post = Post.builder()
        .id(1L)
        .title("Sample Post")
        .content("This is a sample post content.")
        .createdAt(LocalDateTime.now())
        .build();

    postDto = PostDto.builder()
        .id(1L)
        .title("Sample Post")
        .content("This is a sample post content.")
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
        .createdAt(LocalDateTime.now())
        .build();

    comment = Comment.builder()
        .id(1L)
        .post(post)
        .user(user)
        .content("This is a comment.")
        .createdAt(LocalDateTime.now())
        .build();

    commentDto = CommentDto.builder()
        .id(1L)
        .post(postDto)
        .user(userDto)
        .content("This is a comment.")
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Test
  void shouldMapCommentToCommentDto() {
    // Given
    when(postMapper.toPostDto(post)).thenReturn(postDto);
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    CommentDto mappedDto = commentMapper.toCommentDto(comment);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getId()).isEqualTo(comment.getId());
    assertThat(mappedDto.getPost()).isEqualTo(postDto);
    assertThat(mappedDto.getUser()).isEqualTo(userDto);
    assertThat(mappedDto.getContent()).isEqualTo(comment.getContent());
    assertThat(mappedDto.getCreatedAt()).isEqualTo(comment.getCreatedAt());
  }

  @Test
  void shouldMapCommentDtoToComment() {
    // Given
    when(postMapper.toEntity(postDto)).thenReturn(post);
    when(userMapper.toUser(userDto)).thenReturn(user);

    // When
    Comment mappedEntity = commentMapper.toEntity(commentDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getId()).isEqualTo(commentDto.getId());
    assertThat(mappedEntity.getPost()).isEqualTo(post);
    assertThat(mappedEntity.getUser()).isEqualTo(user);
    assertThat(mappedEntity.getContent()).isEqualTo(commentDto.getContent());
    assertThat(mappedEntity.getCreatedAt()).isEqualTo(commentDto.getCreatedAt());
  }

  @Test
  void shouldHandleNullValuesForEntityToDto() {
    // When
    CommentDto mappedDto = commentMapper.toCommentDto(null);

    // Then
    assertThat(mappedDto).isNull();
  }

  @Test
  void shouldHandleNullValuesForDtoToEntity() {
    // When
    Comment mappedEntity = commentMapper.toEntity(null);

    // Then
    assertThat(mappedEntity).isNull();
  }

  @Test
  void shouldHandlePartialNullValuesForEntityToDto() {
    // Given
    Comment partialComment = Comment.builder()
        .id(2L)
        .content("Partial content")
        .build();

    // When
    CommentDto mappedDto = commentMapper.toCommentDto(partialComment);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getId()).isEqualTo(2L);
    assertThat(mappedDto.getContent()).isEqualTo("Partial content");
    assertThat(mappedDto.getPost()).isNull();
    assertThat(mappedDto.getUser()).isNull();
    assertThat(mappedDto.getCreatedAt()).isNull();
  }

  @Test
  void shouldHandlePartialNullValuesForDtoToEntity() {
    // Given
    CommentDto partialCommentDto = CommentDto.builder()
        .id(2L)
        .content("Partial content")
        .build();

    // When
    Comment mappedEntity = commentMapper.toEntity(partialCommentDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getId()).isEqualTo(2L);
    assertThat(mappedEntity.getContent()).isEqualTo("Partial content");
    assertThat(mappedEntity.getPost()).isNull();
    assertThat(mappedEntity.getUser()).isNull();
    assertThat(mappedEntity.getCreatedAt()).isNull();
  }
}
