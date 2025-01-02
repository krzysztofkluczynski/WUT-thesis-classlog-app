package com.example.classlog.repository;


import com.example.classlog.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByClassEntityClass_Id(Long classId);
}
