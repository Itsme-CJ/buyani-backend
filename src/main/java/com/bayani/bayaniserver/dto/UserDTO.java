package com.bayani.bayaniserver.dto;

import lombok.Data;

@Data
public class UserDTO {
  private Integer userId;
  private String firstName;
  private String lastName;
  private String email;
  private String role;
  private String phoneNumber;
  private String storeId;
  private String password;
  private Integer pin;
}