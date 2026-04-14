package com.bayani.bayaniserver.dto;

import lombok.Data;

@Data
public class OtpDTO {
  String number;
  String email;
  Integer userId;
  Integer code;
}