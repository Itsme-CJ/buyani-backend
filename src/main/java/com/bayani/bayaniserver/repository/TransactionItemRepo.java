package com.bayani.bayaniserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayani.bayaniserver.entity.TransactionItem;

public interface TransactionItemRepo extends JpaRepository<TransactionItem, Integer> {
  Optional<TransactionItem> findByTransactionItemId(Integer transactionItemId);
}
