package com.example.classlog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.entity.Class;
import com.example.classlog.entity.Post;
import com.example.classlog.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PostRepositoryTest {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private ClassRepository classRepository;

  @Autowired
  private UserRepository userRepository;

  private Class classEntity;
  private User user;

  @BeforeEach
  void setUp() {
    // Given
    classEntity = classRepository.save(Class.builder()
        .name("Test Class")
        .build());

    user = userRepository.save(User.builder()
        .name("John")
        .surname("Doe")
        .email("user@example.com")
        .password("password123")
        .createdAt(LocalDateTime.now())
        .build());

    postRepository.save(Post.builder()
        .assignedClass(classEntity)
        .user(user)
        .title("Test Post")
        .content("Content")
        .createdAt(LocalDateTime.now())
        .build());
  }

  @Test
  void shouldFindPostsByClassId() {
    // When
    List<Post> posts = postRepository.findByAssignedClass_Id(classEntity.getId());

    // Then
    assertThat(posts).hasSize(1);
    assertThat(posts.get(0).getTitle()).isEqualTo("Test Post");
  }
}
