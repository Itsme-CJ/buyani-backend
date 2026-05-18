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
@Table(name = "[address]")
public class Address extends Abstract {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private Integer addressId;

	@Column(name = "first_address")
	private String firstAddress;

	@Column(name = "second_address")
	private String secondAddress;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "postal_code")
	private String postalCode;

	@Column(name = "pin_location")
	private String pinLocation;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id")
	private User user;
}
