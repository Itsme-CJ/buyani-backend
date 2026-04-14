package com.bayani.bayaniserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bayani.bayaniserver.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Integer> {
  Optional<Role> findByUserEmail(String email);

  @Query(value ="SELECT * " +
        "FROM role r " +
        "LEFT JOIN user u " +
        "ON r.role_id = u.role_id " +
        "WHERE u.user_id = :userId", nativeQuery = true)
  Optional<Role> findByUserUserId(Integer userId);

  Optional<Role> findByName(String name);
}
