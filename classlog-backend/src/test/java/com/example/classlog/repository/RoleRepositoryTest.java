package com.example.classlog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.classlog.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RoleRepositoryTest {

  @Autowired
  private RoleRepository roleRepository;

  @BeforeEach
  void setUp() {
    // Given
    roleRepository.save(
        Role.builder()
            .roleName("ROLE_ADMIN")
            .build()
    );

    roleRepository.save(
        Role.builder()
            .roleName("ROLE_USER")
            .build()
    );
  }

  @Test
  void shouldGetRoleByRoleName() {
    // When
    Role role = roleRepository.getByRoleName("ROLE_ADMIN");

    // Then
    assertThat(role).isNotNull();
    assertThat(role.getRoleName()).isEqualTo("ROLE_ADMIN");
  }
}
