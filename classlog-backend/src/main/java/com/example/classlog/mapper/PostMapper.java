package com.example.classlog.mapper;

import com.example.classlog.dto.PostDto;
import com.example.classlog.entities.Post;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PostMapper {

    PostDto toPostDto(Post entity);

    Post toEntity(PostDto postDto);

}
