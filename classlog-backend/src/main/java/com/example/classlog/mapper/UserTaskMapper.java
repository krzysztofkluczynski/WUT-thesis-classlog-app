package com.example.classlog.mapper;

import com.example.classlog.dto.UserTaskDto;
import com.example.classlog.entity.UserTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TaskMapper.class})
public interface UserTaskMapper {


  @Mapping(target = "userTaskId", source = "id")
  UserTaskDto toDto(UserTask userTask);

  @Mapping(target = "id", source = "userTaskId")
  UserTask toEntity(UserTaskDto userTaskDto);
}
