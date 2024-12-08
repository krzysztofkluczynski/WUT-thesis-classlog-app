package com.example.classlog.mapper;

import com.example.classlog.dto.AnswerDto;
import com.example.classlog.entities.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface AnswerMapper {

    // Map Answer entity to AnswerDto
    @Mapping(target = "id", source = "answer.answerId")
    @Mapping(target = "question", source = "answer.question") // Map nested Question object
    AnswerDto toAnswerDto(Answer answer);

    // Map AnswerDto to Answer entity
    @Mapping(target = "answerId", source = "dto.id")
    @Mapping(target = "question", source = "dto.question") // Map nested Question object
    Answer toEntity(AnswerDto dto);
}