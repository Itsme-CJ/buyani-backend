package com.bayani.bayaniserver.dto;

import lombok.Data;

@Data
public class ProductItemDTO {
  private Integer productItemId;
  private String productNumber;
  private String name;
  private String genericName;
  private String type;
  private Float price;
  private String category;
  private Integer stock;
  private Integer quantity;
  private String description;
  private Integer tempStock;
  private Integer criticalLevel;
  private Integer withPrescription;
}