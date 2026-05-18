package com.buyani.buyaniserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import com.buyani.buyaniserver.entity.ProductItem;

public interface ProductItemRepo extends JpaRepository<ProductItem, Integer> {
  Optional<ProductItem> findByProductNumber(String productNumber);
  
  Optional<ProductItem> findByProductItemId(Integer productId);

  @Transactional
  @Modifying
  @Query("UPDATE ProductItem p SET p.status = :status WHERE p.productItemId = :id")
  void updateStatus(@Param("id") Integer id, @Param("status") String status);

  @Transactional
  @Modifying
  @Query("UPDATE ProductItem p SET p.stock = GREATEST(0, p.stock - :qty) WHERE p.productItemId = :id")
  void decrementStock(@Param("id") Integer id, @Param("qty") Integer qty);

  @RestResource(path="search-products")
  @Query("SELECT p FROM ProductItem p WHERE p.storeId = :storeId AND (p.productNumber LIKE %:productNumber% OR p.name LIKE %:name% OR p.genericName LIKE %:genericName%)")
  Page<ProductItem> findByStoreIdAndProductNumberContainingOrNameContainingOrGenericNameContaining(
    @Param("storeId") Integer storeId, 
    @Param("productNumber") String productNumber, 
    @Param("name") String name, 
    @Param("genericName") String genericName, 
  Pageable page);

  @RestResource(path="search-products-list")
  @Query("SELECT p FROM ProductItem p WHERE p.productNumber LIKE %:productNumber% OR p.name LIKE %:name% OR p.genericName LIKE %:genericName%")
  Page<ProductItem> findProductNumberContainingOrNameContainingOrGenericNameContaining(
    @Param("productNumber") String productNumber, 
    @Param("name") String name, 
    @Param("genericName") String genericName, 
  Pageable page);

  @RestResource(path="search-products-list-with-stock")
  @Query("SELECT p FROM ProductItem p WHERE (p.productNumber LIKE %:productNumber% OR p.name LIKE %:name% OR p.genericName LIKE %:genericName%) AND p.stock != 0")
  Page<ProductItem> findProductNumberContainingOrNameContainingOrGenericNameContainingWithStock(
    @Param("productNumber") String productNumber, 
    @Param("name") String name, 
    @Param("genericName") String genericName, 
  Pageable page);

  @RestResource(path="search-products-with-stock")
  @Query("SELECT p FROM ProductItem p WHERE p.stock != 0")
  Page<ProductItem> findWithStock(
    @Param("productNumber") String productNumber, 
    @Param("name") String name, 
    @Param("genericName") String genericName, 
  Pageable page);

  Page<ProductItem> findByStoreId(Integer storeId, Pageable page);

  List<ProductItem> findByStoreId(Integer storeId);

  @RestResource(path="search-products-category")
  Page<ProductItem> findByProductNumberContainingOrNameContainingAndStoreIdAndProductCategoryName(String productNumber, 
  String name, Integer storeId, String category, Pageable page);

  @RestResource(path="category")
  Page<ProductItem> findByStoreIdAndProductCategoryName(Integer storeId, String category, Pageable page);

  @Query("SELECT p FROM ProductItem p WHERE p.productNumber IN :productNumbers")
  List<ProductItem> findAllByProductNumber(List<String> productNumbers);

  @Query(value ="SELECT * FROM product_item WHERE store_id  = :storeId AND with_prescription = :pres", nativeQuery = true)
  List<ProductItem> findAllStoreIdAndWithPrescription(Integer storeId, Integer pres);

  @Query(value = "SELECT "
    + "   COUNT(*) AS total_items "
    + "FROM "
    + "   product_item WHERE stock = 0 OR stock <= critical_level AND store_id = :storeId", nativeQuery = true)
  Long count(Integer storeId);



}
