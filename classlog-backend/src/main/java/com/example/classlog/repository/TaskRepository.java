package com.example.classlog.repository;

import com.example.classlog.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCreatedBy_IdOrderByCreatedAtDesc(Long id);
}
