package com.lawencon.payroll.dto.company;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompanyReqDto {
    private String companyName;
    private String fileContent;
    private String fileExtension;
    private String payrollDate;
}
