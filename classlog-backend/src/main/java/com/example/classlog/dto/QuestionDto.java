package com.example.classlog.dto;

import com.example.classlog.entity.QuestionType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDto {

  private Long questionId;
  private QuestionType questionType;
  private LocalDateTime editedAt;
  private Integer points;
  private String content;
  private FileDto file;
}

