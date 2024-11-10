package com.example.classlog.mapper;

import com.example.classlog.dto.ClassDto;
import com.example.classlog.entities.Class;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassMapper {
    ClassDto toClassDto(Class entity);

    Class toEntity(ClassDto classDto);
}