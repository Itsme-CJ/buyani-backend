package com.bayani.bayaniserver.dto;

import com.bayani.bayaniserver.entity.Role;
import com.bayani.bayaniserver.entity.User;

import lombok.Data;

@Data
public class AuthUserDTO {
  User user;
  Role role;
}
