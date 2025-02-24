package com.example.classlog.service;

import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.dto.CommentDto;
import com.example.classlog.entity.Comment;
import com.example.classlog.mapper.CommentMapper;
import com.example.classlog.repository.CommentRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  public List<CommentDto> findAllComments() {
    return commentRepository.findAll().stream()
        .map(commentMapper::toCommentDto)
        .collect(Collectors.toList());
  }


  public CommentDto findCommentById(Long commentId) {
    return commentRepository.findById(commentId)
        .map(commentMapper::toCommentDto)
        .orElse(null);
  }

  public List<CommentDto> findCommentsByPostId(Long postId) {
    return commentRepository.findByPost_Id(postId).stream()
        .map(commentMapper::toCommentDto)
        .collect(Collectors.toList());
  }

  public CommentDto createComment(CommentDto commentDto) {
    if (commentDto == null) {
      throw new AppException("Comment cannot be null", HttpStatus.BAD_REQUEST);
    }
    Comment comment = commentMapper.toEntity(commentDto);
    Comment savedComment = commentRepository.save(comment);
    return commentMapper.toCommentDto(savedComment);
  }


  public void deleteComment(Long commentId) {
    commentRepository.deleteById(commentId);
  }
}

