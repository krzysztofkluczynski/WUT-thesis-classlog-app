package com.example.classlog.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionWithAnswersAndUserAnswerDto {
    QuestionDto question;
    List<AnswerDto> answers;
    String userAnswer;
    Integer score;
}
