package com.example.classlog.dto;

import com.example.classlog.entities.Question;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionWithAnswersDto {
    QuestionDto question;
    List<AnswerDto> answers;
}
