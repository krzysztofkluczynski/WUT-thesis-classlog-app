package com.example.classlog.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LessonPresenceRequest {
    private long lessonId;
    private List<UserDto> users;
}
