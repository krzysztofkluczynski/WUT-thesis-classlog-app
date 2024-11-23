package com.example.classlog.repository;

import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.Presence;
import com.example.classlog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PresenceRepository extends JpaRepository<Presence, Long> {
    boolean existsByLesson_LessonIdAndStudent_Id(long lessonId, Long id);

    @Modifying
    @Query(value = "INSERT INTO presence (lesson_id, student_id) VALUES (:lessonId, :id)", nativeQuery = true)
    void insertPresence(long lessonId, Long id);

    @Query("SELECT p.student FROM Presence p WHERE p.lesson.lessonId = :lessonId")
    List<User> findUsersByLessonId(@Param("lessonId") Long lessonId);

    @Query("SELECT p.student.id FROM Presence p WHERE p.lesson.lessonId = :lessonId")
    List<Long> findStudentIdsByLesson_LessonId(@Param("lessonId") Long lessonId);


    void deleteByLesson_LessonIdAndStudent_Id(Long lessonId, Long studentId);
}
