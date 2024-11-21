package com.example.classlog.mapper;

import com.example.classlog.dto.FileDto;
import com.example.classlog.entities.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClassMapper.class, UserMapper.class})
public interface FileMapper {

    @Mapping(target = "assignedClass", source = "classEntity")
    @Mapping(target = "uploadedBy", source = "userEntity")
    FileDto toFileDto(File entity);

    @Mapping(target = "classEntity", source = "assignedClass")
    @Mapping(target = "userEntity", source = "uploadedBy")
    File toEntity(FileDto fileDto);
}