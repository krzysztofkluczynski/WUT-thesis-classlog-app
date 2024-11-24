package com.example.classlog.repository;

import com.example.classlog.entities.TaskQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskQuestionRepository extends JpaRepository<TaskQuestion, Long> {
}
