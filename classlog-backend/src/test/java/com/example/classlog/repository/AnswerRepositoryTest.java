package com.example.classlog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.classlog.entity.Answer;
import com.example.classlog.entity.Question;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

  @Autowired
  private AnswerRepository answerRepository;

  @Autowired
  private QuestionRepository questionRepository;

  private Question testQuestion;

  @BeforeEach
  void setUp() {
    testQuestion = questionRepository.save(Question.builder()
        .content("Sample question?")
        .build());

    answerRepository.save(Answer.builder()
        .question(testQuestion)
        .isCorrect(true)
        .content("Correct Answer")
        .build());

    answerRepository.save(Answer.builder()
        .question(testQuestion)
        .isCorrect(false)
        .content("Incorrect Answer")
        .build());
  }

  @Test
  void findAllByQuestion_QuestionId() {
    List<Answer> answers = answerRepository.findAllByQuestion_QuestionId(
        testQuestion.getQuestionId());

    assertNotNull(answers);
    assertEquals(2, answers.size());
    assertEquals("Correct Answer", answers.get(0).getContent());
    assertEquals("Incorrect Answer", answers.get(1).getContent());
  }
}
