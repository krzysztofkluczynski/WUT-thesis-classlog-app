package com.example.classlog.mapper;

import com.example.classlog.dto.QuestionDto;
import com.example.classlog.entities.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FileMapper.class})
public interface QuestionMapper {

    // Map Question entity to QuestionDto
    @Mapping(target = "questionType", source = "questionType") // Maps QuestionType directly
    @Mapping(target = "file", source = "file") // Use FileMapper for File to FileDto mapping
    QuestionDto toQuestionDto(Question question);

    // Map QuestionDto to Question entity
    @Mapping(target = "questionType", source = "questionType") // Maps QuestionType directly
    @Mapping(target = "file", source = "file") // Use FileMapper for FileDto to File mapping
    Question toEntity(QuestionDto questionDto);
}
