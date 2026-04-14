package com.bayani.bayaniserver.entity;

import java.util.Date;
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
@Table(name = "[reservation]")
public class Reservation extends Abstract {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_id")
	private Integer reservationId;

	@Column(name = "note")
	private String note;

	@Column(name = "status")
	private Integer status;

	@Column(name = "date_claimed")
	private Date dateClaimed;

	@Column(name = "total_price")
	private Float totalPrice;

	@Column(name = "transaction_id")
	private Integer transactionId;

	@Column(name = "reference")
	private String reference;

	@Column(name = "schedule_day")
	private Date scheduleDay;

	@Column(name = "schedule_time")
	private Date scheduleTime;

	@JsonIgnore
  @ManyToOne
  @JoinColumn(name = "store_id")
	private Store store;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "reservation")
	private List<ReservationItem> reservationItems;
}
