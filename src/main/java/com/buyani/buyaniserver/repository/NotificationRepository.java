// repository/NotificationRepository.java
package com.buyani.buyaniserver.repository;

import com.buyani.buyaniserver.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
  List<Notification> findByEmailOrderByCreatedAtDesc(String email);
}