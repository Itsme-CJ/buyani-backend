package com.buyani.buyaniserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance
@Entity
@Table(name = "[transaction_item]")
public class TransactionItem extends Abstract implements IEntity {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_item_id")
	private Integer transactionItemId;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "transaction_id")
	private Transaction transaction;

	@Column(name = "product_item_id")
	private Integer productItemId;

	@Column(name = "quantity")
	private Integer quantity;
}
