package com.example.classlog.mapper;

import com.example.classlog.dto.CommentDto;
import com.example.classlog.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PostMapper.class, UserMapper.class})
public interface CommentMapper {

  CommentDto toCommentDto(Comment comment);

  Comment toEntity(CommentDto commentDto);

}
