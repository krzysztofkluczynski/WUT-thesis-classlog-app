package com.example.classlog.repository;

import com.example.classlog.entity.SubmittedAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmittedAnswerRepository extends JpaRepository<SubmittedAnswer, Long> {

    List<SubmittedAnswer> findByTaskQuestion_Task_IdAndUser_Id(Long taskId, Long userId);
}
