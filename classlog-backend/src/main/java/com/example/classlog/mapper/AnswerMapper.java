package com.example.classlog.mapper;

import com.example.classlog.dto.AnswerDto;
import com.example.classlog.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface AnswerMapper {

  @Mapping(target = "id", source = "answer.answerId")
  @Mapping(target = "question", source = "answer.question")
  AnswerDto toAnswerDto(Answer answer);

  @Mapping(target = "answerId", source = "dto.id")
  @Mapping(target = "question", source = "dto.question")
  Answer toEntity(AnswerDto dto);
}