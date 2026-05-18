package com.buyani.buyaniserver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.buyani.buyaniserver.entity.OpeningHour;

public interface OpeningHourRepo extends JpaRepository<OpeningHour, Integer> {
  List<OpeningHour> findByStoreStoreId(Integer storeId);
}
