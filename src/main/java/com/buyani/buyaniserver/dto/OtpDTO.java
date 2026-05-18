package com.buyani.buyaniserver.dto;

import lombok.Data;

@Data
public class OtpDTO {
  String number;
  String email;
  Integer userId;
  Integer code;
}