package com.example.classlog.repository;

import com.example.classlog.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCreatedBy_IdOrderByCreatedAtDesc(Long id);

    @Query("SELECT t FROM Task t JOIN UserTask ut ON t.id = ut.task.id " +
            "WHERE ut.user.id = :userId AND t.dueDate > CURRENT_TIMESTAMP " +
            "AND NOT EXISTS (SELECT sa FROM SubmittedAnswer sa WHERE sa.user.id = ut.user.id AND sa.taskQuestion.task.id = t.id)")
    List<Task> findTasksTodoByUser(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t JOIN UserTask ut ON t.id = ut.task.id " +
            "WHERE ut.user.id = :userId AND EXISTS (SELECT sa FROM SubmittedAnswer sa WHERE sa.user.id = ut.user.id AND sa.taskQuestion.task.id = t.id)")
    List<Task> findTasksDoneByUser(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t JOIN UserTask ut ON t.id = ut.task.id " +
            "WHERE ut.user.id = :userId AND t.dueDate < CURRENT_TIMESTAMP " +
            "AND NOT EXISTS (SELECT sa FROM SubmittedAnswer sa WHERE sa.user.id = ut.user.id AND sa.taskQuestion.task.id = t.id)")
    List<Task> findOverdueTasksNotSubmittedByUser(@Param("userId") Long userId);
}
