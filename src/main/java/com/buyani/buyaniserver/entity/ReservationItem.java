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
@Table(name = "[reservation_item]")
public class ReservationItem extends Abstract {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_item_id")
	private Integer reservationItemId;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "product_item_id")
	private Integer productItemId;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "reservation_id")
	private Reservation reservation;
}
