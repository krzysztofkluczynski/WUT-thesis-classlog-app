package com.example.classlog.mapper;

import com.example.classlog.dto.TaskDto;
import com.example.classlog.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TaskMapper {

  @Mapping(target = "createdBy", source = "entity.createdBy")
  @Mapping(target = "createdAt", source = "entity.createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
  TaskDto toTaskDto(Task entity);

  @Mapping(target = "createdBy", source = "taskDto.createdBy")
  Task toEntity(TaskDto taskDto);
}
