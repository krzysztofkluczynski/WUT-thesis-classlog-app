package com.example.classlog.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.classlog.dto.PostDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entity.Post;
import com.example.classlog.entity.User;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PostMapperTest {

  @Mock
  private UserMapper userMapper;

  private PostMapperImpl postMapper;

  private Post post;
  private PostDto postDto;
  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // Manually instantiate the PostMapper and inject UserMapper
    postMapper = new PostMapperImpl();

    Field userMapperField = PostMapperImpl.class.getDeclaredField("userMapper");
    userMapperField.setAccessible(true);
    userMapperField.set(postMapper, userMapper);

    // Initialize entities and DTOs
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

    post = Post.builder()
        .id(1L)
        .user(user)
        .title("First Post")
        .content("This is the first post content.")
        .createdAt(LocalDateTime.of(2024, 12, 20, 10, 0))
        .build();

    postDto = PostDto.builder()
        .id(1L)
        .user(userDto)
        .title("First Post")
        .content("This is the first post content.")
        .createdAt(LocalDateTime.of(2024, 12, 20, 10, 0))
        .build();
  }

  @Test
  void shouldMapPostToPostDto() {
    // Given
    when(userMapper.toUserDto(user)).thenReturn(userDto);

    // When
    PostDto mappedDto = postMapper.toPostDto(post);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getId()).isEqualTo(post.getId());
    assertThat(mappedDto.getUser()).isEqualTo(userDto);
    assertThat(mappedDto.getTitle()).isEqualTo(post.getTitle());
    assertThat(mappedDto.getContent()).isEqualTo(post.getContent());
    assertThat(mappedDto.getCreatedAt()).isEqualTo(post.getCreatedAt());
  }

  @Test
  void shouldMapPostDtoToPost() {
    // Given
    when(userMapper.toUser(userDto)).thenReturn(user);

    // When
    Post mappedEntity = postMapper.toEntity(postDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getId()).isEqualTo(postDto.getId());
    assertThat(mappedEntity.getUser()).isEqualTo(user);
    assertThat(mappedEntity.getTitle()).isEqualTo(postDto.getTitle());
    assertThat(mappedEntity.getContent()).isEqualTo(postDto.getContent());
    assertThat(mappedEntity.getCreatedAt()).isEqualTo(postDto.getCreatedAt());
  }

  @Test
  void shouldHandleNullValuesForEntityToDto() {
    // When
    PostDto mappedDto = postMapper.toPostDto(null);

    // Then
    assertThat(mappedDto).isNull();
  }

  @Test
  void shouldHandleNullValuesForDtoToEntity() {
    // When
    Post mappedEntity = postMapper.toEntity(null);

    // Then
    assertThat(mappedEntity).isNull();
  }

  @Test
  void shouldHandlePartialNullValuesForEntityToDto() {
    // Given
    Post partialPost = Post.builder()
        .id(2L)
        .title("Partial Post")
        .build();

    // When
    PostDto mappedDto = postMapper.toPostDto(partialPost);

    // Then
    assertThat(mappedDto).isNotNull();
    assertThat(mappedDto.getId()).isEqualTo(2L);
    assertThat(mappedDto.getTitle()).isEqualTo("Partial Post");
    assertThat(mappedDto.getContent()).isNull();
    assertThat(mappedDto.getUser()).isNull();
    assertThat(mappedDto.getCreatedAt()).isNull();
  }

  @Test
  void shouldHandlePartialNullValuesForDtoToEntity() {
    // Given
    PostDto partialPostDto = PostDto.builder()
        .id(2L)
        .title("Partial Post")
        .build();

    // When
    Post mappedEntity = postMapper.toEntity(partialPostDto);

    // Then
    assertThat(mappedEntity).isNotNull();
    assertThat(mappedEntity.getId()).isEqualTo(2L);
    assertThat(mappedEntity.getTitle()).isEqualTo("Partial Post");
    assertThat(mappedEntity.getContent()).isNull();
    assertThat(mappedEntity.getUser()).isNull();
    assertThat(mappedEntity.getCreatedAt()).isNull();
  }
}
