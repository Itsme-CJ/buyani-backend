package com.bayani.bayaniserver.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.bayani.bayaniserver.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(@Param("email") String email);

  @Query(value ="SELECT * " +
      "FROM user r " +
      "WHERE u.email = :email", nativeQuery = true)
  Optional<User> findByUser(String email);

  Optional<User> findByUserId(Integer userId);

  Boolean existsByEmail(String email);

  Boolean existsByPhoneNumber(String phoneNumber);

  Optional<User> findByPhoneNumber(String phoneNumber);

  User findByPin(Integer pin);

  Page<User> findByStoreId(String storeId, Pageable page);

  @Query(value = "SELECT * FROM user u LEFT JOIN role r ON  u.role_id = r.role_id WHERE r.name = :name", nativeQuery = true)
  Page<User> findByRoleName(String name, Pageable page);

  @RestResource(path="user-search")
  Page<User> findByLastNameContainingOrFirstNameContainingAndStoreId(String lastName, String firstName, String storeId, Pageable page);

  @RestResource(path="system-admin-search")
  @Query(value = "SELECT * FROM user u LEFT JOIN role r ON u.role_id = r.role_id WHERE r.name = :name AND (u.last_name LIKE CONCAT('%', :lastName, '%') OR u.first_name LIKE CONCAT('%', :firstName, '%'))", nativeQuery = true)
  Page<User> findByRoleNameAndLastNameContainingOrFirstNameContaining(String name, String lastName, String firstName, Pageable page);
}
