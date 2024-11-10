package com.example.classlog.controller;

import com.example.classlog.dto.CommentDto;
import com.example.classlog.dto.PostDto;
import com.example.classlog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllPosts() {
        List<CommentDto> comments = commentService.findAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getPostById(@PathVariable Long commentId) {
        CommentDto comment = commentService.findCommentById(commentId);
        return ResponseEntity.ok(comment);
    }
}
