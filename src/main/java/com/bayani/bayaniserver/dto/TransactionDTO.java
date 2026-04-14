package com.bayani.bayaniserver.dto;

import java.util.List;

import lombok.Data;

@Data
public class TransactionDTO {
  List<ProductItemDTO> productNumbers;
  Float totalPrice;
  Integer storeId;
  Integer status;
  Float discount;
  Float cash;
  Float change;
  Integer userId;
  Integer trasactionId;
  String customerName;
  String customerId;
  String authorizedBy;
  String transactionNum;

  ReservationDTO reservation;
}
