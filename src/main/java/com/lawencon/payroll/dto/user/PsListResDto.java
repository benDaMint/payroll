package com.lawencon.payroll.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PsListResDto {
    private String psId;
    private String userName;
    private String email;
    private String phoneNo;
    private Integer totalClients;
    private String profilePictureId;
}
