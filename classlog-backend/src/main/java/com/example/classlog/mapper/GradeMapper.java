package com.example.classlog.mapper;

import com.example.classlog.dto.GradeDto;
import com.example.classlog.entity.Grade;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ClassMapper.class, UserMapper.class})
public interface GradeMapper {

  GradeDto toGradeDto(Grade grade);

  Grade toEntity(GradeDto gradeDto);
}
