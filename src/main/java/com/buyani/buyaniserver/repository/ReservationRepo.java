package com.buyani.buyaniserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

import com.buyani.buyaniserver.entity.Reservation;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
  Optional<Reservation> findByTransactionId(Integer transactionId);

  Optional<Reservation> findByReservationId(Integer transactionId);

  @RestResource(path="search-reservation")
  Page<Reservation> findByUserFirstNameContainingOrUserLastNameContainingAndStoreStoreId(String firstName, 
  String lastName, Integer storeId, Pageable page);

  Page<Reservation> findByStoreStoreId(Integer storeId, Pageable page);

  List<Reservation> findByUserUserId(Integer userId);
  
  @Query(value = "SELECT "
              + "   COUNT(*) "
              + "FROM "
              + "   reservation WHERE store_id = :storeId", nativeQuery = true)
  Long count(Integer storeId);

  @Query(value = "SELECT "
              + "   * "
              + "FROM "
              + "   reservation WHERE store_id = :storeId AND status = 1", nativeQuery = true)
  Page<Reservation> findRecentReservation(Integer storeId, Pageable page);

}
