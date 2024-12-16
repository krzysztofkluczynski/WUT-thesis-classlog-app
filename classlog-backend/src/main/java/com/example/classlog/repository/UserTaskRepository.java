package com.example.classlog.repository;

import com.example.classlog.entities.Task;
import com.example.classlog.entities.User;
import com.example.classlog.entities.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    boolean existsByTaskAndUser(Task task, User user);

    List<UserTask> findByUser_Id(Long id);

    Optional<UserTask> findByUser_IdAndTask_Id(Long id, Long id1);

    @Query("""
        SELECT ut
        FROM UserTask ut
        JOIN ut.task t
        WHERE t.createdBy.id = :createdById
          AND EXISTS (
              SELECT 1
              FROM SubmittedAnswer sa
              JOIN sa.taskQuestion tq
              WHERE tq.task.id = t.id
                AND sa.user.id = ut.user.id
          )
    """)
    List<UserTask> findTasksCreatedByUserWithSubmittedAnswers(@Param("createdById") Long createdById);

    @Query("""
        SELECT ut
        FROM UserTask ut
        JOIN ut.task t
        WHERE t.createdBy.id = :createdById
          AND t.dueDate < CURRENT_TIMESTAMP
          AND NOT EXISTS (
              SELECT 1
              FROM SubmittedAnswer sa
              JOIN sa.taskQuestion tq
              WHERE tq.task.id = t.id
                AND sa.user.id = ut.user.id
          )
    """)
    List<UserTask> findOverdueTasksNotSubmittedByCreatedByUser(@Param("createdById") Long createdById);
}
