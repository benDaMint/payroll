package com.lawencon.payroll.service;

import java.util.List;

import com.lawencon.payroll.dto.company.CompanyReqDto;
import com.lawencon.payroll.dto.company.CompanyResDto;
import com.lawencon.payroll.dto.company.UpdateCompanyReqDto;
import com.lawencon.payroll.dto.generalResponse.UpdateResDto;
import com.lawencon.payroll.model.Company;

public interface CompanyService {
    Company createCompany(CompanyReqDto data);

    Company findByCompanyName(String companyName);

    List<CompanyResDto> getCompanies();

    UpdateResDto updateCompany(UpdateCompanyReqDto data);
}
