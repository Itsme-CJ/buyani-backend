package com.bayani.bayaniserver.dto;

import lombok.Data;

@Data
public class StoreDTO {
  private String name;
	private String image;
	private String status;
	private Integer isReservationActivated;
	private String firstAddress;
	private String secondAddress;
	private String city;
	private String state;
	private String postalCode;
	private String pinLocation;
	private String phoneNumber;
	private String email;
	private String description;
	private Integer userId;
  UserDTO user;
}
