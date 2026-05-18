package com.buyani.buyaniserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buyani.buyaniserver.entity.TransactionItem;

public interface TransactionItemRepo extends JpaRepository<TransactionItem, Integer> {
  Optional<TransactionItem> findByTransactionItemId(Integer transactionItemId);
}
