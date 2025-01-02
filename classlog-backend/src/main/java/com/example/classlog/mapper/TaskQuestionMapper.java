package com.example.classlog.mapper;

import com.example.classlog.dto.TaskQuestionDto;
import com.example.classlog.entity.TaskQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TaskMapper.class, QuestionMapper.class})
public interface TaskQuestionMapper {

    // Map TaskQuestion entity to TaskQuestionDto
    @Mapping(target = "task", source = "task") // Use TaskMapper
    @Mapping(target = "question", source = "question") // Use QuestionMapper
    @Mapping(target = "taskQuestionId", source = "taskQuestionId") // Direct mapping for ID
    TaskQuestionDto toDto(TaskQuestion taskQuestion);

    // Map TaskQuestionDto to TaskQuestion entity
    @Mapping(target = "task", source = "task") // Use TaskMapper
    @Mapping(target = "question", source = "question") // Use QuestionMapper
    @Mapping(target = "taskQuestionId", source = "taskQuestionId") // Direct mapping for ID
    TaskQuestion toEntity(TaskQuestionDto taskQuestionDto);
}
