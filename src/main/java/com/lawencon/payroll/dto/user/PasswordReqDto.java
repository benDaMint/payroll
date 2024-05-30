package com.lawencon.payroll.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordReqDto {
    private String oldPassword;
    private String newPassword;
}
