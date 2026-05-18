package com.buyani.buyaniserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.buyani.buyaniserver.entity.AuditLog;

public interface AuditLogRepo extends JpaRepository<AuditLog, Integer> {
  Page<AuditLog> findByStoreIdIsNull(Pageable page);

  Page<AuditLog> findByStoreId(Integer storeId, Pageable page);
}
