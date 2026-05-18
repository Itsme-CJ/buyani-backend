package com.buyani.buyaniserver.dto;

import com.buyani.buyaniserver.entity.Role;
import com.buyani.buyaniserver.entity.User;

import lombok.Data;

@Data
public class AuthUserDTO {
  User user;
  Role role;
}
