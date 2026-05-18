package com.buyani.buyaniserver.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ReservationDTO {
  private Integer reservationId;

	private String note;

	private Integer status;

	private Date dateClaimed;

	private Float totalPrice;

	private Integer transactionId;

	private UserDTO user;

	private String reference;
}
