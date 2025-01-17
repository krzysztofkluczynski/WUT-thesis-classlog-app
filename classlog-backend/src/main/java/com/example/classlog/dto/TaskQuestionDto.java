package com.example.classlog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskQuestionDto {

    private Long taskQuestionId;
    private TaskDto task;
    private QuestionDto question;
}
