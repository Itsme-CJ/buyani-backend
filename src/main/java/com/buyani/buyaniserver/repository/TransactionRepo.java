package com.buyani.buyaniserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.buyani.buyaniserver.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Integer> {

  Optional<Transaction> findByTransactionId(Integer transactionId);


  @Query(value = "SELECT * FROM transaction WHERE status NOT IN (:statuses) AND store_id = :storeId ", nativeQuery = true)
  Page<Transaction> findAllStatusNotIn(List<Integer> statuses, Integer storeId, Pageable page);

  @Query(value = "SELECT "
    + "   SUM(total_price) "
    + "FROM "
    + "   transaction WHERE store_id = :storeId AND status = 1", nativeQuery = true)
  Float count(Integer storeId);
}
