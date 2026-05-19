package com.buyani.buyaniserver.entity;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Inheritance
@Entity
@Table(name = "opening_hour")
public class OpeningHour extends Abstract {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "opening_hour_id")
	private Integer openingHourId;

	@Column(name = "store_id")
	private String storeId;

	@Column(name = "day")
	private String day;

	@Column(name = "from_hour")
	private Date fromHour;
	
	@Column(name = "from_minute")
	private Date fromMinute;

	@Column(name = "until_hour")
	private Date untilHour;

	@Column(name = "until_minute")
	private Date untilMinute;

	@ManyToOne
	@JoinColumn(name = "store_id", nullable = true, updatable = false, insertable = false)
	private Store store;
}
