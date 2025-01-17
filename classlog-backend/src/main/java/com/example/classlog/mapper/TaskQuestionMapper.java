package com.example.classlog.mapper;

import com.example.classlog.dto.TaskQuestionDto;
import com.example.classlog.entity.TaskQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TaskMapper.class, QuestionMapper.class})
public interface TaskQuestionMapper {

    @Mapping(target = "task", source = "task")
    @Mapping(target = "question", source = "question")
    @Mapping(target = "taskQuestionId", source = "taskQuestionId")
    TaskQuestionDto toDto(TaskQuestion taskQuestion);

    @Mapping(target = "task", source = "task")
    @Mapping(target = "question", source = "question")
    @Mapping(target = "taskQuestionId", source = "taskQuestionId")
    TaskQuestion toEntity(TaskQuestionDto taskQuestionDto);
}
