package com.example.classlog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmittedAnswerDto {
    private Long submittedAnswerId;
    private TaskQuestionDto taskQuestion; // Reference to TaskQuestionDto
    private UserDto user; // Reference to UserDto
    private LocalDateTime createdAt;
    private String content;
}
