package com.example.classlog.service;

import com.example.classlog.dto.PostDto;
import com.example.classlog.entities.Post;
import com.example.classlog.mapper.PostMapper;
import com.example.classlog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public List<PostDto> findAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::toPostDto)
                .collect(Collectors.toList());
    }

    public PostDto findPostById(Long postId) {
        return postRepository.findById(postId)
                .map(postMapper::toPostDto)
                .orElse(null);
    }

    public List<PostDto> findPostByClassId(Long classId) {
        return postRepository.findByAssignedClass_Id(classId).stream()
                .map(postMapper::toPostDto)
                .collect(Collectors.toList());
    }

    public PostDto createPost(PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        Post savedPost = postRepository.save(post);
        return postMapper.toPostDto(savedPost);
    }
}