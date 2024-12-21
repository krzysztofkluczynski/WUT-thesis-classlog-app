package com.example.classlog.mapper;

import com.example.classlog.dto.PostDto;
import com.example.classlog.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PostMapper {

  @Mapping(target = "user", source = "entity.user")
    // Map the user field using UserMapper
  PostDto toPostDto(Post entity);

  @Mapping(target = "user", source = "postDto.user")
    // Map the user field using UserMapper
  Post toEntity(PostDto postDto);
}
