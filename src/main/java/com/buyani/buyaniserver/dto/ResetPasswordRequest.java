package com.buyani.buyaniserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String code;
    private String newPassword;
    private Integer userId;
    private String currentPassword;
}

