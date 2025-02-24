package com.example.classlog.repository;

import com.example.classlog.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudent_Id(Long studentId);
    List<Grade> findByAssignedClass_IdOrderByCreatedAtDesc(Long classId);
}
