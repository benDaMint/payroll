package com.lawencon.payroll.dto.company;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateCompanyReqDto {
    private String id;
    private String companyName;
    private String companyLogoContent;
    private String companyLogoExtension;
}
