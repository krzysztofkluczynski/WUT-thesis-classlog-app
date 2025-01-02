package com.example.classlog.repository;

import com.example.classlog.entity.TaskQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskQuestionRepository extends JpaRepository<TaskQuestion, Long> {
    List<TaskQuestion> findAllByTaskId(long taskId);

    @Query("SELECT tq FROM TaskQuestion tq WHERE tq.task.id = :taskId AND tq.question.questionId = :questionId")
    Optional<TaskQuestion> findTaskQuestionByQuestionIdAndTaskId(@Param("questionId") Long questionId, @Param("taskId") Long taskId);


}
