package com.example.classlog.repository;

import com.example.classlog.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByRoleName(String role);
}
