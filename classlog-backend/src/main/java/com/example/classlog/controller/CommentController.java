package com.example.classlog.controller;

import com.example.classlog.dto.CommentDto;
import com.example.classlog.dto.PostDto;
import com.example.classlog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentDto> comments = commentService.findAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long commentId) {
        CommentDto comment = commentService.findCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.findCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentDto> createPost(@RequestBody CommentDto commentDto) {
        CommentDto comment = commentService.createComment(commentDto);
        return ResponseEntity.ok(comment);
    }


}
