package com.example.classlog.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmittedTaskDto {
    private TaskDto task;
    private UserDto user;
    private List<QuestionWithAnswersAndUserAnswerDto> questionsWithAnswers;
}
