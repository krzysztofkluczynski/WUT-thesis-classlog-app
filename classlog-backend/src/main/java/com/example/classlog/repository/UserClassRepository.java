package com.example.classlog.repository;

import com.example.classlog.entities.UserClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserClassRepository extends JpaRepository<UserClass, Long> {

    boolean existsByClassEntity_IdAndUser_Id(Long classId, Long userId);

    @Modifying
    @Query(value = "INSERT INTO user_class (class_id, user_id) VALUES (:classId, :userId)", nativeQuery = true)
    void insertUserIntoClass(@Param("classId") Long classId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM user_class WHERE class_id = :classId AND user_id = :userId", nativeQuery = true)
    void deleteUserFromClass(@Param("classId") Long classId, @Param("userId") Long userId);
}
