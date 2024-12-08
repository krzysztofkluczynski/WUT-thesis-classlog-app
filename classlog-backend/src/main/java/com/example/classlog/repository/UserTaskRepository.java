package com.example.classlog.repository;

import com.example.classlog.entities.Task;
import com.example.classlog.entities.User;
import com.example.classlog.entities.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    boolean existsByTaskAndUser(Task task, User user);

    List<UserTask> findByUser_Id(Long id);
}
