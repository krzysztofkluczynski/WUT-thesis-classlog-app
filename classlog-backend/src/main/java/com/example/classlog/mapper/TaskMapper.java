package com.example.classlog.mapper;

import com.example.classlog.dto.TaskDto;
import com.example.classlog.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TaskMapper {

    // Map Task entity to TaskDto
    @Mapping(target = "createdBy", source = "entity.createdBy") // Use UserMapper to map User to UserDto
    @Mapping(target = "createdAt", source = "entity.createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss") // Format creation date
    TaskDto toTaskDto(Task entity);

    // Map TaskDto to Task entity
    @Mapping(target = "createdBy", source = "taskDto.createdBy") // Use UserMapper to map UserDto to User
    Task toEntity(TaskDto taskDto);
}
