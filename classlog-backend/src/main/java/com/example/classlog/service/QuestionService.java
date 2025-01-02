package com.example.classlog.service;


import com.example.classlog.dto.QuestionDto;
import com.example.classlog.dto.QuestionWithAnswersDto;
import com.example.classlog.entity.Answer;
import com.example.classlog.entity.Question;
import com.example.classlog.entity.Task;
import com.example.classlog.entity.TaskQuestion;
import com.example.classlog.mapper.AnswerMapper;
import com.example.classlog.mapper.QuestionMapper;
import com.example.classlog.repository.AnswerRepository;
import com.example.classlog.repository.QuestionRepository;
import com.example.classlog.repository.TaskQuestionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuestionService {

  private final QuestionRepository questionRepository;

  private final AnswerRepository answerRepository;

  private final TaskQuestionRepository taskQuestionRepository;
  private final QuestionMapper questionMapper;

  private final AnswerMapper answerMapper;

  public QuestionDto createQuestion(QuestionDto questionDto) {
    Question question = questionRepository.save(questionMapper.toEntity(questionDto));
    return questionMapper.toQuestionDto(question);
  }

  public QuestionDto getQuestionById(Long id) {
    return questionRepository.findById(id)
        .map(questionMapper::toQuestionDto)
        .orElse(null);
  }


  public void deleteQuestion(Long id) {
    questionRepository.deleteById(id);
  }

  public List<QuestionDto> getAllQuestions() {
    return questionRepository.findAll().stream()
        .map(questionMapper::toQuestionDto)
        .collect(java.util.stream.Collectors.toList());
  }

  public List<QuestionDto> createQuestionWithAnswers(
      List<QuestionWithAnswersDto> questionWithAnswersDtos, long taskId) {
    return questionWithAnswersDtos.stream()
        .map(questionWithAnswersDto -> {
          Question savedQuestion = questionRepository.save(
              questionMapper.toEntity(questionWithAnswersDto.getQuestion())
          );

          // Map and save each answer associated with the question
          List<Answer> answers = questionWithAnswersDto.getAnswers().stream()
              .map(answerDto -> {
                Answer answer = answerMapper.toEntity(answerDto);
                answer.setQuestion(savedQuestion); // Associate the answer with the saved question
                return answer;
              })
              .collect(Collectors.toList());

          answerRepository.saveAll(answers); // Batch save all answers

          // Create and save the TaskQuestion record
          TaskQuestion taskQuestion = TaskQuestion.builder()
              .task(Task.builder().id(taskId).build()) // Create a task reference with taskId
              .question(savedQuestion) // Associate with the saved question
              .build();

          taskQuestionRepository.save(taskQuestion);

          // Return the saved question as a DTO
          return questionMapper.toQuestionDto(savedQuestion);
        })
        .collect(Collectors.toList()); // Collect all saved QuestionDtos into a list
  }


  public List<QuestionDto> assignQuestionsToTask(List<Long> questionIds, long taskId) {
    // Fetch the Task entity using the provided taskId
    Task task = Task.builder().id(taskId).build();

    // Fetch the questions by their IDs
    List<Question> questions = questionRepository.findAllById(questionIds);

    // Map each question to a TaskQuestion and save
    List<TaskQuestion> taskQuestions = questions.stream()
        .map(question -> TaskQuestion.builder()
            .task(task) // Associate with the given task
            .question(question) // Associate with the question
            .build())
        .collect(Collectors.toList());

    taskQuestionRepository.saveAll(taskQuestions); // Batch save all TaskQuestion entities

    // Return the questions as a list of QuestionDto
    return questions.stream()
        .map(questionMapper::toQuestionDto)
        .collect(Collectors.toList());
  }

  public List<QuestionWithAnswersDto> getQuestionsWithAnswers(long taskId) {
    // Fetch all TaskQuestion entities associated with the given taskId
    List<TaskQuestion> taskQuestions = taskQuestionRepository.findAllByTaskId(taskId);

    // Map each TaskQuestion to a QuestionWithAnswersDto
    return taskQuestions.stream()
        .map(taskQuestion -> {
          // Fetch the question associated with the TaskQuestion
          Question question = taskQuestion.getQuestion();

          // Fetch all answers associated with the question
          List<Answer> answers = answerRepository.findAllByQuestion_QuestionId(
              question.getQuestionId());

          // Map the question and answers to a QuestionWithAnswersDto
          return QuestionWithAnswersDto.builder()
              .question(questionMapper.toQuestionDto(question))
              .answers(answers.stream()
                  .map(answerMapper::toAnswerDto)
                  .collect(Collectors.toList()))
              .build();
        })
        .collect(Collectors.toList());
  }
}
