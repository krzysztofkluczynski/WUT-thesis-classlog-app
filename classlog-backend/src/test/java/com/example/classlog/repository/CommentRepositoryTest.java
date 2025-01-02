package com.example.classlog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.classlog.entity.Class;
import com.example.classlog.entity.Comment;
import com.example.classlog.entity.Post;
import com.example.classlog.entity.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CommentRepositoryTest {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ClassRepository classRepository;

  private Post post;
  private User user;
  private Class assignedClass;

  @BeforeEach
  void setUp() {
    // Given: Prepare test data
    assignedClass = classRepository.save(
        Class.builder()
            .name("Test Class")
            .description("Test Class Description")
            .code("CLASS123")
            .build()
    );

    user = userRepository.save(
        User.builder()
            .name("John")
            .surname("Doe")
            .email("test@example.com")
            .password("password123")
            .build()
    );

    post = postRepository.save(
        Post.builder()
            .assignedClass(assignedClass) // Associate the Post with the Class
            .user(user)
            .title("Sample Post")
            .content("Sample Content")
            .build()
    );

    commentRepository.save(
        Comment.builder()
            .post(post)
            .user(user)
            .content("Sample Comment")
            .build()
    );
  }

  @Test
  void testFindByPostId() {
    // Given
    Long postId = post.getId();

    // When
    List<Comment> comments = commentRepository.findByPost_Id(postId);

    // Then
    assertNotNull(comments);
    assertEquals(1, comments.size());
    assertEquals("Sample Comment", comments.get(0).getContent());
  }

  @Test
  void testFindById() {
    // Given
    Comment comment = commentRepository.findAll().get(0); // Get the saved comment's ID
    Long commentId = comment.getId();

    // When
    Optional<Comment> foundComment = commentRepository.findById(commentId);

    // Then
    assertTrue(foundComment.isPresent());
    assertEquals("Sample Comment", foundComment.get().getContent());
  }
}
