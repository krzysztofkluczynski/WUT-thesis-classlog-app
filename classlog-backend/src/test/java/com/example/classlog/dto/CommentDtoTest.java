package com.example.classlog.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class CommentDtoTest {

  @Test
  void shouldCreateCommentDtoUsingBuilder() {
    // Given
    Long id = 1L;
    PostDto postDto = PostDto.builder()
        .id(1L)
        .title("Sample Post")
        .content("This is a sample post.")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto userDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    String content = "This is a comment.";
    LocalDateTime createdAt = LocalDateTime.now();

    // When
    CommentDto commentDto = CommentDto.builder()
        .id(id)
        .post(postDto)
        .user(userDto)
        .content(content)
        .createdAt(createdAt)
        .build();

    // Then
    assertThat(commentDto.getId()).isEqualTo(id);
    assertThat(commentDto.getPost()).isEqualTo(postDto);
    assertThat(commentDto.getUser()).isEqualTo(userDto);
    assertThat(commentDto.getContent()).isEqualTo(content);
    assertThat(commentDto.getCreatedAt()).isEqualTo(createdAt);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    CommentDto commentDto = CommentDto.builder().build();

    // When
    PostDto postDto = PostDto.builder()
        .id(1L)
        .title("Sample Post")
        .content("Sample content.")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto userDto = UserDto.builder()
        .id(2L)
        .name("Jane")
        .surname("Doe")
        .email("jane.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    commentDto.setId(1L);
    commentDto.setPost(postDto);
    commentDto.setUser(userDto);
    commentDto.setContent("Test comment");
    commentDto.setCreatedAt(LocalDateTime.now());

    // Then
    assertThat(commentDto.getId()).isEqualTo(1L);
    assertThat(commentDto.getPost()).isEqualTo(postDto);
    assertThat(commentDto.getUser()).isEqualTo(userDto);
    assertThat(commentDto.getContent()).isEqualTo("Test comment");
    assertThat(commentDto.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    PostDto postDto1 = PostDto.builder()
        .id(1L)
        .title("Sample Post")
        .content("Sample content.")
        .createdAt(LocalDateTime.now())  // createdAt will be slightly different between instances
        .build();
    UserDto userDto1 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())  // createdAt will be slightly different between instances
        .build();
    CommentDto commentDto1 = CommentDto.builder()
        .id(1L)
        .post(postDto1)
        .user(userDto1)
        .content("This is a comment.")
        .createdAt(LocalDateTime.now())  // createdAt will be slightly different between instances
        .build();

    PostDto postDto2 = PostDto.builder()
        .id(1L)
        .title("Sample Post")
        .content("Sample content.")
        .createdAt(postDto1.getCreatedAt())  // Ensure same timestamp for comparison
        .build();
    UserDto userDto2 = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(userDto1.getCreatedAt())  // Ensure same timestamp for comparison
        .build();
    CommentDto commentDto2 = CommentDto.builder()
        .id(1L)
        .post(postDto2)
        .user(userDto2)
        .content("This is a comment.")
        .createdAt(commentDto1.getCreatedAt())  // Ensure same timestamp for comparison
        .build();

    // When & Then
    assertThat(commentDto1)
        .isEqualToIgnoringGivenFields(commentDto2, "createdAt", "post.createdAt",
            "user.createdAt");  // Ignore the date fields

    // Optionally, check hashCodes to ensure they match as well
    assertThat(commentDto1.hashCode()).isEqualTo(commentDto2.hashCode());
  }


  @Test
  void shouldTestToString() {
    // Given
    PostDto postDto = PostDto.builder()
        .id(1L)
        .title("Sample Post")
        .content("Sample content.")
        .createdAt(LocalDateTime.now())
        .build();
    UserDto userDto = UserDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();
    CommentDto commentDto = CommentDto.builder()
        .id(1L)
        .post(postDto)
        .user(userDto)
        .content("This is a test comment.")
        .createdAt(LocalDateTime.now())
        .build();

    // When & Then
    assertThat(commentDto.toString()).contains("id=1",
        "content=This is a test comment.");
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    CommentDto commentDto = CommentDto.builder().build();

    // When & Then
    assertThat(commentDto.getId()).isNull();
    assertThat(commentDto.getPost()).isNull();
    assertThat(commentDto.getUser()).isNull();
    assertThat(commentDto.getContent()).isNull();
    assertThat(commentDto.getCreatedAt()).isNull();
  }
}
