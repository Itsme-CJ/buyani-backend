package com.bayani.bayaniserver.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class Abstract implements IEntity {

  @Column(name = "who_added")
  @CreatedBy
  protected Integer whoAdded;

  @Column(name = "who_updated", nullable = true)
  @LastModifiedBy
  protected Integer whoUpdated;

  @Column(name = "when_added")
  @CreationTimestamp
  protected Date whenAdded;

  @Column(name = "ts")
  @UpdateTimestamp
  protected Date ts;

  @Override
  public void setWhoAdded(Integer whoAdded) { this.whoAdded = whoAdded; }

  @Override
  public Integer getWhoAdded() { return whoAdded; }

  @Override
  public void setWhenAdded(Date date) { this.whenAdded = date; }

  @Override
  public Date getWhenAdded() { return whenAdded; }

  @Override
  public void setWhoUpdated(Integer whoUpdated) { this.whoUpdated = whoUpdated; }

  @Override
  public Integer getWhoUpdated() { return whoUpdated; }

  @Override
  public void setTs(Date date) { this.ts = date; }

  @Override
  public Date getTs() { return ts; }
}