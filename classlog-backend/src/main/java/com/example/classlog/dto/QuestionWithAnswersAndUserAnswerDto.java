package com.example.classlog.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionWithAnswersAndUserAnswerDto {

  QuestionDto question;
  List<AnswerDto> answers;
  String userAnswer;
  Integer score;
}
