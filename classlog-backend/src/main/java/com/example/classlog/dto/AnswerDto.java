package com.example.classlog.dto;

import com.example.classlog.entities.Question;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerDto {
    private Long id; // Primary key (answer_id)
    private QuestionDto question; // Foreign key reference to question
    private Boolean isCorrect; // Indicates if the answer is correct
    private String content; // Content of the answer
}