package com.example.classlog.mapper;

import com.example.classlog.dto.FileDto;
import com.example.classlog.entities.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClassMapper.class, UserMapper.class})
public interface FileMapper {

    @Mapping(target = "assignedClass", source = "classEntity")
    @Mapping(target = "uploadedBy", source = "userEntity")
    @Mapping(target = "filePath", expression = "java(normalizePath(entity.getFilePath()))")
    FileDto toFileDto(File entity);

    @Mapping(target = "classEntity", source = "assignedClass")
    @Mapping(target = "userEntity", source = "uploadedBy")
    @Mapping(target = "filePath", expression = "java(normalizePath(fileDto.getFilePath()))")
    File toEntity(FileDto fileDto);

    default String normalizePath(String path) {
        if (path == null) {
            return null;
        }
        return path.replace("\\", "/");
    }
}
