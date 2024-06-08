package com.lawencon.payroll.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientResDto {
    private String id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String profilePictureId;
}
