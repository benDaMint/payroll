package com.lawencon.payroll.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResDto {
    private String id;
    private String userName;
    private String email;
    private String roleName;
    private String phoneNumber;
    private String profilePictureId;
    private String profilePictureContent;
    private String profilePictureExtension;
}
