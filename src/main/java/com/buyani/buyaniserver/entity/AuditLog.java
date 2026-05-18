package com.buyani.buyaniserver.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.buyani.buyaniserver.util.JsonObjectConverter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "audit_log")
public class AuditLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "audit_log_id")
  private Integer auditLogiId;

  @Column(name = "entity_id")
  private Integer entityId;

  @Column(name = "store_id")
  private Integer storeId;
  
  @Column(name = "entity_name")
  private String entityName;

  @Column(name = "operation")
  private String operation;

  @Column(name = "field")
  private String field;

  @Column(name = "previous_value")
  @Convert(converter = JsonObjectConverter.class) 
  private Object previousValue;

  @Column(name = "new_value")
  @Convert(converter = JsonObjectConverter.class) 
  private Object newValue;

  @Column(name = "timestamp")
  @CreationTimestamp
  private Date timeStamp;

  @Column(name = "user_id", updatable = false)
  private String userId;
}
