package com.bayani.bayaniserver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bayani.bayaniserver.entity.OpeningHour;

public interface OpeningHourRepo extends JpaRepository<OpeningHour, Integer> {
  List<OpeningHour> findByStoreStoreId(Integer storeId);
}
