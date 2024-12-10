package com.example.classlog.dto;

import com.example.classlog.entities.Question;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionWithAnswersDto {
    private QuestionDto question;
    private List<AnswerDto> answers;
}
