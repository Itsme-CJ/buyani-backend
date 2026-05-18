package com.buyani.buyaniserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import com.buyani.buyaniserver.entity.Store;

public interface StoreRepo extends JpaRepository<Store, Integer> {
  Optional<Store> findByStoreId(Integer storeId);

  List<Store> findByStatus(String status);
  List<Store> findByNameContaining(String name);
  List<Store> findByEmail(String email);
  
  Boolean existsByName(String name);
  Optional<Store> findByName(String name);
}
