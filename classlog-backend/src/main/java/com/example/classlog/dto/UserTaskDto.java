package com.example.classlog.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTaskDto {
    private Long userTaskId;
    private UserDto task;
    private TaskDto user;
    private Integer score;
}
