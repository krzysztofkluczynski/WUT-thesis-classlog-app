package com.example.classlog.mapper;


import com.example.classlog.dto.LessonDto;
import com.example.classlog.entities.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ClassMapper.class})
public interface LessonMapper {

    @Mapping(target = "createdByUser", source = "entity.createdBy")
    @Mapping(target = "lessonClass", source = "entity.classEntity")
    @Mapping(target = "lessonDate", dateFormat = "yyyy-MM-dd HH:mm:ss") // Adjust format as needed
    LessonDto toLessonDto(Lesson entity);

    @Mapping(target = "createdBy", source = "lessonDto.createdByUser")
    @Mapping(target = "classEntity", source = "lessonDto.lessonClass")
    Lesson toEntity(LessonDto lessonDto);
}

