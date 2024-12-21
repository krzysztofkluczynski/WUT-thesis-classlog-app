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
public class QuestionWithAnswersDto {

  private QuestionDto question;
  private List<AnswerDto> answers;
}
