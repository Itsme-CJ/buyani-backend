package com.bayani.bayaniserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayani.bayaniserver.entity.ReservationList;

public interface ReservationListRepo extends JpaRepository<ReservationList, Integer> {
  
  List<ReservationList> findByUserUserId(Integer userId);

  List<ReservationList> findByStoreId(Integer storeId);
}

