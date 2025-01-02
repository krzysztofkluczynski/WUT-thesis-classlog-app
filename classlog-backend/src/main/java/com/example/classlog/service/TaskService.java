package com.example.classlog.service;


import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.dto.*;
import com.example.classlog.entity.*;
import com.example.classlog.mapper.*;
import com.example.classlog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final AnswerMapper answerMapper;

    private final UserMapper userMapper;

    private final QuestionMapper questionMapper;

    private final UserTaskMapper userTaskMapper;
    private final UserRepository userRepository;
    private final UserTaskRepository userTaskRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    private final TaskQuestionRepository taskQuestionRepository;

    private final SubmittedAnswerRepository submittedAnswerRepository;

    private final UserService userService;

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toTaskDto)
                .collect(java.util.stream.Collectors.toList());
    }

    public Optional<TaskDto> getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toTaskDto);
    }

    public TaskDto createTask(TaskDto task) {
        Task savedTask = taskRepository.save(taskMapper.toEntity(task));
        return taskMapper.toTaskDto(savedTask);
    }

    public Optional<Task> updateTask(Long id, Task taskDetails) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isEmpty()) {
            return Optional.empty();
        }

        Task task = taskOptional.get();
        task.setTaskName(taskDetails.getTaskName());
        task.setDescription(taskDetails.getDescription());
        task.setDueDate(taskDetails.getDueDate());

        Task updatedTask = taskRepository.save(task);
        return Optional.of(updatedTask);
    }

    public boolean deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }
        taskRepository.deleteById(id);
        return true;
    }

    public boolean assignUsersToTask(Long taskId, List<ClassDto> classDtos) {
        // Retrieve the task
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isEmpty()) {
            return false;
        }

        Task task = taskOptional.get();

        // Use a Set to collect unique users
        Set<Long> uniqueUserIds = new HashSet<>();
        List<User> uniqueUsers = classDtos.stream()
                .flatMap(classDto -> userService.getUsersByClass(classDto.getId()).stream()) // Get users from each class
                .filter(userDto -> userDto.getRole() != null && userDto.getRole().getId() == 2) // Filter by roleId = 2
                .filter(userDto -> uniqueUserIds.add(userDto.getId())) // Add to Set; returns false if already present
                .map(userDto -> userRepository.findById(userDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("User with ID " + userDto.getId() + " not found"))) // Convert UserDto to User
                .collect(Collectors.toList());

        // Assign unique users to the task
        uniqueUsers.forEach(user -> {
            UserTask userTask = new UserTask();
            userTask.setTask(task);
            userTask.setUser(user);
            userTask.setScore(0);
            userTaskRepository.save(userTask);
        });

        return true;
    }


    public List<TaskDto> getTasksByCreatedBy(Long id) {
        return taskRepository.findByCreatedBy_IdOrderByCreatedAtDesc(id).stream()
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByUser(Long id) {
        List<UserTask> userTasks = userTaskRepository.findByUser_Id(id);
        return userTasks.stream()
                .map(UserTask::getTask)
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByUserCurrent(Long id) {
        LocalDateTime now = LocalDateTime.now(); // Pobierz aktualną datę i czas
        List<UserTask> userTasks = userTaskRepository.findByUser_Id(id);

        return userTasks.stream()
                .filter(userTask -> userTask.getTask().getDueDate().isAfter(now)) // Filtruj zadania z dueDate > now
                .map(UserTask::getTask)
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksTodoByUser(Long userId) {
        return taskRepository.findTasksTodoByUser(userId).stream()
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksDoneByUser(Long userId) {
        return taskRepository.findTasksDoneByUser(userId).stream()
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getOverdueTasksNotSubmittedByUser(Long userId) {
        return taskRepository.findOverdueTasksNotSubmittedByUser(userId).stream()
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public boolean processSubmittedTask(SubmittedTaskDto submittedTaskDto) {
        // Retrieve the UserTask entry for the given task and user
        Optional<UserTask> userTaskOptional = userTaskRepository.findByUser_IdAndTask_Id(
                submittedTaskDto.getUser().getId(),
                submittedTaskDto.getTask().getId()
        );

        if (userTaskOptional.isEmpty()) {
            return false;
        }

        UserTask userTask = userTaskOptional.get();
        int totalScore = 0;

        // Iterate through the questions and evaluate answers
        for (QuestionWithAnswersAndUserAnswerDto questionWithUserAnswer : submittedTaskDto.getQuestionsWithAnswers()) {
            QuestionDto questionDto = questionWithUserAnswer.getQuestion();
            String userAnswer = questionWithUserAnswer.getUserAnswer();

            // Check if the user's answer matches any correct answer
            boolean isCorrect = questionWithUserAnswer.getAnswers().stream()
                    .anyMatch(answer -> answer.getIsCorrect() && answer.getContent().equals(userAnswer));

            // Add points to total score if the answer is correct
            if (isCorrect) {
                totalScore += questionDto.getPoints();
            }

            // Save the SubmittedAnswer record
            SubmittedAnswer submittedAnswer = new SubmittedAnswer();
            submittedAnswer.setUser(userTask.getUser());
            submittedAnswer.setTaskQuestion(taskQuestionRepository.findTaskQuestionByQuestionIdAndTaskId(questionDto.getQuestionId(), submittedTaskDto.getTask().getId())
                    .orElseThrow(() -> new AppException("Task not submitted", HttpStatus.NOT_FOUND)));
            submittedAnswer.setCreatedAt(LocalDateTime.now());
            submittedAnswer.setContent(userAnswer);
            submittedAnswerRepository.save(submittedAnswer);
        }

        userTask.setScore(totalScore);
        userTaskRepository.save(userTask);

        return true;
    }

    public Optional<SubmittedTaskDto> getSubmittedTaskDetails(Long taskId, Long userId) {
        // Check if the user and task relationship exists
        Optional<UserTask> userTaskOptional = userTaskRepository.findByUser_IdAndTask_Id(userId, taskId);

        if (userTaskOptional.isEmpty()) {
            return Optional.empty();
        }

        UserTask userTask = userTaskOptional.get();

        // Fetch submitted answers for the given task and user
        List<SubmittedAnswer> submittedAnswers = submittedAnswerRepository.findByTaskQuestion_Task_IdAndUser_Id(taskId, userId);

        // Map submitted answers to QuestionWithAnswersAndUserAnswerDto
        List<QuestionWithAnswersAndUserAnswerDto> questionsWithAnswers = submittedAnswers.stream()
                .map(submittedAnswer -> {
                    TaskQuestion taskQuestion = submittedAnswer.getTaskQuestion();

                    // Fetch answers for the question from AnswerRepository
                    List<Answer> answersForQuestion = answerRepository.findAllByQuestion_QuestionId(taskQuestion.getQuestion().getQuestionId());

                    // Map answers to AnswerDto
                    List<AnswerDto> answerDtos = answersForQuestion.stream()
                            .map(answerMapper::toAnswerDto)
                            .collect(Collectors.toList());

                    Question question = questionRepository.findById(taskQuestion.getQuestion().getQuestionId())
                            .orElseThrow(() -> new AppException("Question not found", HttpStatus.NOT_FOUND));

                    // Build and return QuestionWithAnswersAndUserAnswerDto
                    return QuestionWithAnswersAndUserAnswerDto.builder()
                            .question(questionMapper.toQuestionDto(question))
                            .answers(answerDtos)
                            .userAnswer(submittedAnswer.getContent())
                            .score(answerDtos.stream()
                                    .filter(AnswerDto::getIsCorrect) // Find the correct answer
                                    .anyMatch(correctAnswer -> correctAnswer.getContent().equals(submittedAnswer.getContent()))
                                    ? taskQuestion.getQuestion().getPoints() // If the user answer matches, assign points
                                    : 0)// Otherwise, assign 0
                    .build();
                })
                .collect(Collectors.toList());


        // Create SubmittedTaskDto
        SubmittedTaskDto submittedTaskDto = SubmittedTaskDto.builder()
                .task(taskMapper.toTaskDto(userTask.getTask()))
                .user(userMapper.toUserDto(userTask.getUser()))
                .questionsWithAnswers(questionsWithAnswers)
                .score(userTask.getScore())
                .build();

        return Optional.of(submittedTaskDto);
    }


    public List<UserTaskDto> getTasksCreatedByUserSubmitted(Long id) {
        List<UserTask> userTasks = userTaskRepository.findTasksCreatedByUserWithSubmittedAnswers(id);

        return userTasks.stream()
                .map(userTaskMapper::toDto)
                .peek(dto -> dto.setScore(dto.getScore() != null ? dto.getScore() : 0)) // Ensure score is set if null
                .collect(Collectors.toList());
    }



    public List<UserTaskDto> getOverdueTasksNotSubmitttedCreatedByUser(Long id) {
        List<UserTask> userTasks = userTaskRepository.findOverdueTasksNotSubmittedByCreatedByUser(id);

        userTasks.forEach(userTask -> {
            if (userTask.getScore() == null) {
                userTask.setScore(0); // Explicitly set score to 0 for overdue tasks
                userTaskRepository.save(userTask); // Save the updated score to the database
            }
        });

        return userTasks.stream()
                .map(userTaskMapper::toDto)
                .collect(Collectors.toList());
    }


    public boolean updateUserTaskScore(Long userId, Long taskId, Integer newScore) {
        Optional<UserTask> userTaskOptional = userTaskRepository.findByUser_IdAndTask_Id(userId, taskId);
        if (userTaskOptional.isPresent()) {
            UserTask userTask = userTaskOptional.get();
            userTask.setScore(newScore);
            userTaskRepository.save(userTask);
            return true;
        } else {
            return false;
        }
    }
}