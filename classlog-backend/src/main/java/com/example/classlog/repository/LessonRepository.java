package com.example.classlog.repository;

import com.example.classlog.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCreatedBy_IdOrderByLessonDateDesc(Long userId);


    List<Lesson> findByClassEntity_Id(Long classId);

    @Query("""
           SELECT l
           FROM Lesson l
           JOIN UserClass uc ON l.classEntity.id = uc.classEntity.id
           WHERE uc.user.id = :userId
           ORDER BY l.lessonDate DESC
           """)

    List<Lesson> findLessonsByUserId(@Param("userId") Long userId);
}
