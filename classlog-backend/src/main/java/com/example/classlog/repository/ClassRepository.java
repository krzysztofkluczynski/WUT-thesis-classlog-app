package com.example.classlog.repository;

import com.example.classlog.entities.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassRepository extends JpaRepository<Class, Long> {

    @Query("SELECT c FROM Class c JOIN UserClass uc ON c.id = uc.id.classId WHERE uc.id.userId = :userId")
    List<Class> findByUserId(@Param("userId") Long userId);
}