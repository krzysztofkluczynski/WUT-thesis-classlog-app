package com.example.classlog.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleTest {

  private Role role;

  @BeforeEach
  void setUp() {
    // Setting up test data for Role
    role = new Role(1L, "Admin");
  }

  @Test
  void shouldCreateRoleUsingConstructor() {
    // Given
    Long roleId = 1L;
    String roleName = "Admin";

    // When
    Role role = new Role(roleId, roleName);

    // Then
    assertThat(role.getId()).isEqualTo(roleId);
    assertThat(role.getRoleName()).isEqualTo(roleName);
  }

  @Test
  void shouldCreateRoleUsingBuilder() {
    // Given
    Long roleId = 1L;
    String roleName = "Admin";

    // When
    Role role = Role.builder()
        .id(roleId)
        .roleName(roleName)
        .build();

    // Then
    assertThat(role.getId()).isEqualTo(roleId);
    assertThat(role.getRoleName()).isEqualTo(roleName);
  }

  @Test
  void shouldTestGettersAndSetters() {
    // Given
    Role role = new Role();

    // When
    Long roleId = 1L;
    String roleName = "Admin";

    role.setId(roleId);
    role.setRoleName(roleName);

    // Then
    assertThat(role.getId()).isEqualTo(roleId);
    assertThat(role.getRoleName()).isEqualTo(roleName);
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    // Given
    Role role1 = Role.builder()
        .id(1L)
        .roleName("Admin")
        .build();

    Role role2 = Role.builder()
        .id(1L)
        .roleName("Admin")
        .build();

    // When & Then
    assertThat(role1).isEqualTo(role2);
    assertThat(role1.hashCode()).isEqualTo(role2.hashCode());
  }

  @Test
  void shouldTestToString() {
    // Given
    Role role = Role.builder()
        .id(1L)
        .roleName("Admin")
        .build();

    // When & Then
    String toString = role.toString();

    // Check if the toString method includes the relevant fields
    assertThat(toString).contains("id=1", "roleName=Admin");
  }

  @Test
  void shouldHandleNullValuesForConstructor() {
    // Given
    Role role = new Role();

    // When & Then
    assertThat(role.getId()).isNull();
    assertThat(role.getRoleName()).isNull();
  }

  @Test
  void shouldHandleNullValuesForBuilder() {
    // Given
    Role role = Role.builder().build();

    // When & Then
    assertThat(role.getId()).isNull();
    assertThat(role.getRoleName()).isNull();
  }
}
