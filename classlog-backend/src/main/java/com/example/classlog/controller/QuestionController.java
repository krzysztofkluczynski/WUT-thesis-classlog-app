package com.example.classlog.controller;

import com.example.classlog.dto.QuestionDto;
import com.example.classlog.dto.QuestionWithAnswersDto;
import com.example.classlog.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionDto> createQuestion(@RequestBody QuestionDto questionDto) {
        QuestionDto createdQuestion = questionService.createQuestion(questionDto);
        return ResponseEntity.ok(createdQuestion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable Long id) {
        QuestionDto questionDto = questionService.getQuestionById(id);
        return ResponseEntity.ok(questionDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<QuestionDto>> getAllQuestions() {
        List<QuestionDto> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/withAnswers/{taskId}")
    public ResponseEntity<List<QuestionDto>> createQuestionWithAnswers(@PathVariable long taskId, @RequestBody List<QuestionWithAnswersDto> questionWithAnswersDto) {
        List<QuestionDto> createdQuestion = questionService.createQuestionWithAnswers(questionWithAnswersDto, taskId);
        return ResponseEntity.ok(createdQuestion);
    }

    @PostMapping("/assignQuestionsToTask/{taskId}")
    public ResponseEntity<List<QuestionDto>> assignQuestionsToTask(@PathVariable long taskId, @RequestBody List<Long> questionIds) {
        List<QuestionDto> createdQuestion = questionService.assignQuestionsToTask(questionIds, taskId);
        return ResponseEntity.ok(createdQuestion);
    }
}