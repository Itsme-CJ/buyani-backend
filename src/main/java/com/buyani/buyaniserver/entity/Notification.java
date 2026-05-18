// entity/Notification.java
package com.buyani.buyaniserver.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notification")
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "email")
  private String email;

  @Column(name = "title")
  private String title;

  @Column(name = "message")
  private String message;

  @Column(name = "type")
  private String type; // "APPROVED", "REJECTED", "PENDING", "info"

  @Column(name = "is_read")
  private Boolean isRead = false;

  @Column(name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();
}