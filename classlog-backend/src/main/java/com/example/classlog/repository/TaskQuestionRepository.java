package com.example.classlog.repository;

import com.example.classlog.entities.TaskQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskQuestionRepository extends JpaRepository<TaskQuestion, Long> {
    List<TaskQuestion> findAllByTaskId(long taskId);
}
