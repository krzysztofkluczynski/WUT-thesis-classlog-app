package com.example.classlog.mapper;

import com.example.classlog.dto.SubmittedAnswerDto;
import com.example.classlog.entities.SubmittedAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TaskQuestionMapper.class, UserMapper.class})
public interface SubmittedAnswerMapper {

    // Map SubmittedAnswer entity to SubmittedAnswerDto
    @Mapping(target = "taskQuestion", source = "taskQuestion") // Use TaskQuestionMapper
    @Mapping(target = "user", source = "user") // Use UserMapper
    @Mapping(target = "submittedAnswerId", source = "submittedAnswerId") // Direct mapping for ID
    SubmittedAnswerDto toDto(SubmittedAnswer submittedAnswer);

    // Map SubmittedAnswerDto to SubmittedAnswer entity
    @Mapping(target = "taskQuestion", source = "taskQuestion") // Use TaskQuestionMapper
    @Mapping(target = "user", source = "user") // Use UserMapper
    @Mapping(target = "submittedAnswerId", source = "submittedAnswerId") // Direct mapping for ID
    SubmittedAnswer toEntity(SubmittedAnswerDto submittedAnswerDto);
}
