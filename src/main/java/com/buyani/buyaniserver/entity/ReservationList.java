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
@Table(name = "reservation_list")
public class ReservationList extends Abstract {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_list_id")
	private Integer reservationListId;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "product_item_id")
	private Integer productItemId;
	
	@Column(name = "store_id")
	private Integer storeId;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id")
	private User user;
}
