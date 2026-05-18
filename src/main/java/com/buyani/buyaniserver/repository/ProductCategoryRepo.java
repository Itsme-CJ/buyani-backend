package com.buyani.buyaniserver.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.buyani.buyaniserver.entity.ProductCategory;

public interface ProductCategoryRepo extends JpaRepository<ProductCategory, Integer> {
  Optional<ProductCategory> findByProductCategoryId(Integer storeId);

  Page<ProductCategory> findByStoreStoreId(Integer storeId, Pageable page);
  
  Optional<ProductCategory> findByName(String name);
  
  Optional<ProductCategory> findByNameAndStoreStoreId(String name, Integer storeId);

  Page<ProductCategory> findByNameContainingAndStoreStoreId(String name, Integer storeId, Pageable page);
}
