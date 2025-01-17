package com.example.classlog.mapper;

import com.example.classlog.dto.QuestionDto;
import com.example.classlog.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FileMapper.class})
public interface QuestionMapper {

  @Mapping(target = "questionType", source = "questionType")
  @Mapping(target = "file", source = "file")
  @Mapping(target = "content", source = "content")
  QuestionDto toQuestionDto(Question question);

  @Mapping(target = "questionType", source = "questionType")
  @Mapping(target = "file", source = "file")
  @Mapping(target = "content", source = "content")
  Question toEntity(QuestionDto questionDto);
}

