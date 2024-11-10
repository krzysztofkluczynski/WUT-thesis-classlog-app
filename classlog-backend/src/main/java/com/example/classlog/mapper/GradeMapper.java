package com.example.classlog.mapper;

import com.example.classlog.dto.GradeDto;
import com.example.classlog.entities.Grade;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GradeMapper {

    GradeDto toGradeDto(Grade grade);   // No custom mappings required since field types match directly

    Grade toEntity(GradeDto gradeDto);
}
