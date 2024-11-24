package com.example.classlog.dto;

import com.example.classlog.entities.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private FileDto fileId;
}

