package com.lawencon.payroll.dto.user;

import com.lawencon.payroll.dto.company.CompanyReqDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserReqDto {
    private String userName;
    private String email;
    private String phoneNumber;
    private String roleId;
    private String fileContent;
    private String fileExtension;
    private CompanyReqDto companyReq;
}
