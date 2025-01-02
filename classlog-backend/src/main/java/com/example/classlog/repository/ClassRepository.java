package com.example.classlog.repository;

import com.example.classlog.entity.Class;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {

  @Query("SELECT c FROM Class c JOIN UserClass uc ON c.id = uc.id.classId WHERE uc.id.userId = :userId")
  List<Class> findByUserId(@Param("userId") Long userId);

  Optional<Class> findByCode(String classCode);
}