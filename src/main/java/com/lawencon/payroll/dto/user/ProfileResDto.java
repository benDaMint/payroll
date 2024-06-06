package com.lawencon.payroll.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileResDto {
    private String userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private String roleName;
    private String profilePictureContent;
    private String profilePictureExtension;
}
