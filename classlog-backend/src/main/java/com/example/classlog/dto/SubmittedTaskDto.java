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
public class SubmittedTaskDto {

  private TaskDto task;
  private UserDto user;
  private List<QuestionWithUserAnswerDto> questionsWithAnswers;
  Integer score;
}
