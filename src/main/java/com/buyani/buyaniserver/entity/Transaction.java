package com.buyani.buyaniserver.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance
@Entity
@Table(name = "[transaction]")
public class Transaction extends Abstract implements IEntity {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private Integer transactionId;

	@Column(name = "total_price")
	private Float totalPrice;

	@Column(name = "cash")
	private Float cash;

	@Column(name = "transaction_change")
	private Float change;

	@Column(name = "discount")
	private Float discount;

	@Column(name = "status")
	private Integer status;

	@Column(name = "cust_name")
	private String customerName;

	@Column(name = "cust_id")
	private String customerId;

	@Column(name = "authorized_by")
	private String authorizedBy;

	@Column(name = "transaction_num")
	private String transactionNum;

	@JsonIgnore
  @ManyToOne
  @JoinColumn(name = "store_id")
	private Store store;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "transaction")
	private List<TransactionItem> transactionItems;
}
