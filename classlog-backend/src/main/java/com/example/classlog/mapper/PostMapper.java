package com.example.classlog.mapper;

import com.example.classlog.dto.PostDto;
import com.example.classlog.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PostMapper {

  @Mapping(target = "user", source = "entity.user")
  PostDto toPostDto(Post entity);

  @Mapping(target = "user", source = "postDto.user")
  Post toEntity(PostDto postDto);
}
