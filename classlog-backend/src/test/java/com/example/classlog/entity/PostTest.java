package com.example.classlog.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostTest {

  private Class assignedClass;
  private User user;
  private Post post;

  @BeforeEach
  void setUp() {
    // Setting up test data for Class and User entities
    assignedClass = Class.builder().id(101L).name("Math 101").build();
    user = User.builder().id(1L).name("John").surname("Doe").email("john.doe@example.com").build();
    post = Post.builder()
        .id(1L)
        .assignedClass(assignedClass)
        .user(user)
        .title("Sample Post")
        .content("This is a sample post content.")
        .createdAt(LocalDateTime.MIN)
        .build();
  }

  @Test
  void shouldCreatePostUsingConstructor() {
    // Given
    Long postId = 1L;
    String title = "Sample Post";
    String content = "This is a sample post content.";

    // When
    Post post = new Post(postId, assignedClass, user, title, content, LocalDateTime.MIN);

    // Then
    assertThat(post.getId()).isEqualTo(postId);
    assertThat(post.getAssignedClass()).isEqualTo(assignedClass);
    assertThat(post.getUser()).isEqualTo(user);
    assertThat(post.getTitle()).isEqualTo(title);
    assertThat(post.getContent()).isEqualTo(content);
    assertThat(post.getCreatedAt()).isEqualTo(LocalDateTime.MIN);
  }

  @Test
  void shouldCreatePostUsingBuilder() {
    // Given
    Long postId = 1L;
    String title = "Sample Post";
    String content = "This is a sample post content.";

    // When
    Post post = Post.builder()
        .id(postId)
        .assignedClass(assignedClass)
        .user(user)
        .title(title)
        .content(content)
        .createdAt(LocalDateTime.MIN)
        .build();

    // Then
    assertThat(post.getId()).isEqualTo(postId);
    assertThat(post.getAssignedClass()).isEqualTo(assignedClass);
    assertThat(post.getUser()).isEqualTo(user);
    assertThat(post.getTitle()).isEqualTo(title);
    assertThat(post.getContent()).isEqualTo(content);
    assertThat(post.getCreatedAt()).isEqualTo(LocalDateTime.MIN);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    Post post = new Post();

    // When
    post.setId(1L);
    post.setAssignedClass(assignedClass);
    post.setUser(user);
    post.setTitle("Sample Post");
    post.setContent("This is a sample post content.");
    post.setCreatedAt(LocalDateTime.MIN);

    // Then
    assertThat(post.getId()).isEqualTo(1L);
    assertThat(post.getAssignedClass()).isEqualTo(assignedClass);
    assertThat(post.getUser()).isEqualTo(user);
    assertThat(post.getTitle()).isEqualTo("Sample Post");
    assertThat(post.getContent()).isEqualTo("This is a sample post content.");
    assertThat(post.getCreatedAt()).isEqualTo(LocalDateTime.MIN);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    Post post1 = Post.builder()
        .id(1L)
        .assignedClass(assignedClass)
        .user(user)
        .title("Sample Post")
        .content("This is a sample post content.")
        .createdAt(LocalDateTime.MIN)
        .build();

    Post post2 = Post.builder()
        .id(1L)
        .assignedClass(assignedClass)
        .user(user)
        .title("Sample Post")
        .content("This is a sample post content.")
        .createdAt(LocalDateTime.MIN)
        .build();

    // When & Then
    assertThat(post1).isEqualTo(post2);
    assertThat(post1.hashCode()).isEqualTo(post2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    Post post = Post.builder()
        .id(1L)
        .assignedClass(assignedClass)
        .user(user)
        .title("Sample Post")
        .content("This is a sample post content.")
        .createdAt(LocalDateTime.MIN)
        .build();

    // When & Then
    String toString = post.toString();
    assertThat(toString).contains("id=1", "title=Sample Post",
        "content=This is a sample post content.");
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    Post post = new Post();

    // When & Then
    assertThat(post.getId()).isNull();
    assertThat(post.getAssignedClass()).isNull();
    assertThat(post.getUser()).isNull();
    assertThat(post.getTitle()).isNull();
    assertThat(post.getContent()).isNull();
    assertThat(post.getCreatedAt()).isNull();
  }

  @Test
  void shouldHandleNullValuesForBuilder() {
    // Given
    Post post = Post.builder().build();

    // When & Then
    assertThat(post.getId()).isNull();
    assertThat(post.getAssignedClass()).isNull();
    assertThat(post.getUser()).isNull();
    assertThat(post.getTitle()).isNull();
    assertThat(post.getContent()).isNull();
    assertThat(post.getCreatedAt()).isNull();
  }

  @Test
  void shouldTestPrePersist() {
    // Given
    Post post = new Post();
    post.setAssignedClass(assignedClass);
    post.setUser(user);
    post.setTitle("Sample Post");
    post.setContent("This is a sample post content.");

    // When: Trigger the persistence lifecycle event
    post.onCreate(); // This will set the createdAt field

    // Then
    assertThat(post.getCreatedAt()).isNotNull();
    assertThat(post.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
  }
}
