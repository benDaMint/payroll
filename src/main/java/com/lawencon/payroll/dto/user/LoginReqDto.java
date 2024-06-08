package com.lawencon.payroll.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginReqDto {
    private String email;
    private String password;
}
