package com.example.classlog.mapper;

import com.example.classlog.dto.SubmittedAnswerDto;
import com.example.classlog.entity.SubmittedAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TaskQuestionMapper.class, UserMapper.class})
public interface SubmittedAnswerMapper {

  @Mapping(target = "taskQuestion", source = "taskQuestion")
  @Mapping(target = "user", source = "user")
  @Mapping(target = "submittedAnswerId", source = "submittedAnswerId")
  SubmittedAnswerDto toDto(SubmittedAnswer submittedAnswer);

  @Mapping(target = "taskQuestion", source = "taskQuestion")
  @Mapping(target = "user", source = "user")
  @Mapping(target = "submittedAnswerId", source = "submittedAnswerId")
  SubmittedAnswer toEntity(SubmittedAnswerDto submittedAnswerDto);
}
