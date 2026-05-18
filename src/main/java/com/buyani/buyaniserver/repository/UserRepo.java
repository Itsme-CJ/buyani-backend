package com.buyani.buyaniserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.buyani.buyaniserver.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(@Param("email") String email);

  Optional<User> findByUserId(Integer userId);

  Boolean existsByEmail(String email);

  Boolean existsByPhoneNumber(String phoneNumber);

  Optional<User> findByPhoneNumber(String phoneNumber);

  User findByPin(Integer pin);

  Optional<User> findFirstByStoreId(String storeId); // 👈 added

  Page<User> findByStoreId(String storeId, Pageable page);

  @Query(value = "SELECT * FROM user u LEFT JOIN role r ON u.role_id = r.role_id WHERE r.name = :name", nativeQuery = true)
  Page<User> findByRoleName(@Param("name") String name, Pageable page);

  @RestResource(path = "user-search")
  Page<User> findByLastNameContainingOrFirstNameContainingAndStoreId(
      String lastName, String firstName, String storeId, Pageable page);

  @RestResource(path = "system-admin-search")
  @Query(value = "SELECT * FROM user u LEFT JOIN role r ON u.role_id = r.role_id " +
                 "WHERE r.name = :name " +
                 "AND (u.last_name LIKE CONCAT('%', :lastName, '%') " +
                 "OR u.first_name LIKE CONCAT('%', :firstName, '%'))",
         nativeQuery = true)
  Page<User> findByRoleNameAndLastNameContainingOrFirstNameContaining(
      @Param("name") String name,
      @Param("lastName") String lastName,
      @Param("firstName") String firstName,
      Pageable page);

  @Query(value =
      "SELECT MONTH(u.when_added) as month, COUNT(u.user_id) as cnt " +
      "FROM user u " +
      "LEFT JOIN role r ON u.role_id = r.role_id " +
      "WHERE r.name = :roleName AND YEAR(u.when_added) = :year " +
      "GROUP BY MONTH(u.when_added) " +
      "ORDER BY MONTH(u.when_added)",
      nativeQuery = true)
  List<Object[]> countMonthlySignupsByRole(@Param("roleName") String roleName, @Param("year") int year);

  @Query(value =
      "SELECT COUNT(u.user_id) " +
      "FROM user u " +
      "LEFT JOIN role r ON u.role_id = r.role_id " +
      "WHERE r.name = :roleName",
      nativeQuery = true)
  long countByRoleName(@Param("roleName") String roleName);
}