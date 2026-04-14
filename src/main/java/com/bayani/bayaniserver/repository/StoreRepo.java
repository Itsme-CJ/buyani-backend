package com.bayani.bayaniserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import com.bayani.bayaniserver.entity.Store;

public interface StoreRepo extends JpaRepository<Store, Integer> {
  Optional<Store> findByStoreId(Integer storeId);

  List<Store> findByStatus(String status);
  List<Store> findByNameContaining(String name);
  Optional<Store> findByEmail(String email);
  
  Boolean existsByName(String name);
}
