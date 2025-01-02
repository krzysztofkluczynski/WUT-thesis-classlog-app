package com.example.classlog.mapper;

import com.example.classlog.dto.FileDto;
import com.example.classlog.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClassMapper.class, UserMapper.class})
public interface FileMapper {

  @Mapping(target = "assignedClass", source = "classEntity")
  @Mapping(target = "uploadedBy", source = "userEntity")
  @Mapping(target = "filePath", expression = "java(entity.getFilePath() == null ? null : entity.getFilePath().replace(\"\\\\\", \"/\"))")
  FileDto toFileDto(File entity);

  @Mapping(target = "classEntity", source = "assignedClass")
  @Mapping(target = "userEntity", source = "uploadedBy")
  @Mapping(target = "filePath", expression = "java(fileDto.getFilePath() == null ? null : fileDto.getFilePath().replace(\"\\\\\", \"/\"))")
  File toEntity(FileDto fileDto);
}
