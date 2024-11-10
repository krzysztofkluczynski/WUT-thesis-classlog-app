package com.example.classlog.repository;


import com.example.classlog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRoleId(Long roleId);

    @Query("SELECT u FROM User u JOIN UserClass uc ON u.id = uc.id.userId WHERE uc.id.classId = :classId")
    List<User> findByClassId(@Param("classId") long classId);
}
