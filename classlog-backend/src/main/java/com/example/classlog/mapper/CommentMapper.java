package com.example.classlog.mapper;

import com.example.classlog.dto.CommentDto;
import com.example.classlog.entities.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDto toCommentDto(Comment comment);

    Comment toEntity(CommentDto commentDto);

}
