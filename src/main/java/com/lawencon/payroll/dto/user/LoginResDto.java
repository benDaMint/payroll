package com.lawencon.payroll.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResDto {
    private String userId;
    private String userName;
    private String roleCode;
    private String fileId;
    private String token;
}
