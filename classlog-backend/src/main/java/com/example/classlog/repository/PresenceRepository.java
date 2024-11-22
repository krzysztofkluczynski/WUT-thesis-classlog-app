package com.example.classlog.repository;

import com.example.classlog.entities.Presence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PresenceRepository extends JpaRepository<Presence, Long> {
    boolean existsByLesson_LessonIdAndStudent_Id(long lessonId, Long id);

    @Modifying
    @Query(value = "INSERT INTO presence (lesson_id, student_id) VALUES (:lessonId, :id)", nativeQuery = true)
    void insertPresence(long lessonId, Long id);
}
