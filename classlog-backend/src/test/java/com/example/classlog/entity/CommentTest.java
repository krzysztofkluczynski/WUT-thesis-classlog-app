package com.example.classlog.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentTest {

  private Post post;
  private User user;

  @BeforeEach
  void setUp() {
    // Setting up test data for Post and User entities
    post = Post.builder().id(1L).title("Sample Post").content("This is a sample post.").build();
    user = User.builder().id(1L).name("John").surname("Doe").email("john.doe@example.com").build();
    Comment comment = Comment.builder()
        .id(1L)
        .post(post)
        .user(user)
        .content("This is a comment.")
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Test
  void shouldCreateCommentUsingConstructor() {
    // Given
    Long commentId = 1L;
    String content = "This is a comment.";

    // When
    Comment comment = new Comment(commentId, post, user, content, LocalDateTime.now());

    // Then
    assertThat(comment.getId()).isEqualTo(commentId);
    assertThat(comment.getPost()).isEqualTo(post);
    assertThat(comment.getUser()).isEqualTo(user);
    assertThat(comment.getContent()).isEqualTo(content);
    assertThat(comment.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldCreateCommentUsingBuilder() {
    // Given
    Long commentId = 1L;
    String content = "This is a comment.";

    // When
    Comment comment = Comment.builder()
        .id(commentId)
        .post(post)
        .user(user)
        .content(content)
        .createdAt(LocalDateTime.now())
        .build();

    // Then
    assertThat(comment.getId()).isEqualTo(commentId);
    assertThat(comment.getPost()).isEqualTo(post);
    assertThat(comment.getUser()).isEqualTo(user);
    assertThat(comment.getContent()).isEqualTo(content);
    assertThat(comment.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    Comment comment = new Comment();

    // When
    comment.setId(1L);
    comment.setPost(post);
    comment.setUser(user);
    comment.setContent("This is a comment.");
    comment.setCreatedAt(LocalDateTime.now());

    // Then
    assertThat(comment.getId()).isEqualTo(1L);
    assertThat(comment.getPost()).isEqualTo(post);
    assertThat(comment.getUser()).isEqualTo(user);
    assertThat(comment.getContent()).isEqualTo("This is a comment.");
    assertThat(comment.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    Comment comment1 = Comment.builder()
        .id(1L)
        .post(post)
        .user(user)
        .content("This is a comment.")
        .createdAt(LocalDateTime.now())
        .build();

    Comment comment2 = Comment.builder()
        .id(1L)
        .post(post)
        .user(user)
        .content("This is a comment.")
        .createdAt(LocalDateTime.now())
        .build();

    // When & Then
    assertThat(comment1).isEqualTo(comment2);
    assertThat(comment1.hashCode()).isEqualTo(comment2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    Comment comment = Comment.builder()
        .id(1L)
        .post(post)
        .user(user)
        .content("This is a comment.")
        .createdAt(LocalDateTime.now())
        .build();

    // When & Then
    String toString = comment.toString();
    assertThat(toString).contains("id=1", "content=This is a comment.", "post=", "user=");
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    Comment comment = new Comment();

    // When & Then
    assertThat(comment.getId()).isNull();
    assertThat(comment.getPost()).isNull();
    assertThat(comment.getUser()).isNull();
    assertThat(comment.getContent()).isNull();
    assertThat(comment.getCreatedAt()).isNull();
  }

  @Test
  void shouldHandleNullValuesForBuilder() {
    // Given
    Comment comment = Comment.builder().build();

    // When & Then
    assertThat(comment.getId()).isNull();
    assertThat(comment.getPost()).isNull();
    assertThat(comment.getUser()).isNull();
    assertThat(comment.getContent()).isNull();
    assertThat(comment.getCreatedAt()).isNull();
  }

  @Test
  void shouldTestPrePersist() {
    // Given
    Comment comment = new Comment();
    comment.setPost(post);
    comment.setUser(user);
    comment.setContent("This is a comment.");

    // When: Trigger the persistence lifecycle event
    comment.onCreate(); // This will set the createdAt field

    // Then
    assertThat(comment.getCreatedAt()).isNotNull();
    assertThat(comment.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
  }
}
