package com.example.classlog.repository;

import com.example.classlog.entities.Comment;
import com.example.classlog.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_Id(Long postId);
}
