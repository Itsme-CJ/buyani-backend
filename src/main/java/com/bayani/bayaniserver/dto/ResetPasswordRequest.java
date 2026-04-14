package com.bayani.bayaniserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String code;
    private String newPassword;
    private Integer userId;
}

