package com.lawencon.payroll.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.lawencon.payroll.dto.company.CompanyReqDto;
import com.lawencon.payroll.dto.company.CompanyResDto;
import com.lawencon.payroll.dto.company.UpdateCompanyReqDto;
import com.lawencon.payroll.dto.generalResponse.UpdateResDto;
import com.lawencon.payroll.exception.FailCheckException;
import com.lawencon.payroll.model.Company;
import com.lawencon.payroll.repository.CompanyRepository;
import com.lawencon.payroll.service.CompanyService;
import com.lawencon.payroll.service.FileService;
import com.lawencon.payroll.service.PrincipalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final FileService fileService;
    private final PrincipalService principalService;

    @Override
    public Company createCompany(CompanyReqDto data) {
        final String id = principalService.getUserId();

        final var file = fileService.saveFile(data.getFileContent(), data.getFileExtension());

        final var company = new Company();

        company.setCompanyName(data.getCompanyName());
        company.setCompanyLogo(file);

        company.setPayrollDate(Integer.valueOf(data.getPayrollDate()));

        company.setCreatedBy(id);

        return companyRepository.save(company);
    }

    @Override
    public Company findByCompanyName(String companyName) {
        return companyRepository.findByCompanyName(companyName);
    }

    @Override
    public List<CompanyResDto> getCompanies() {
        final var companiesRes = new ArrayList<CompanyResDto>();

        final var companies = companyRepository.findAll();

        companies.forEach(company -> {
            final var companyRes = new CompanyResDto();

            companyRes.setId(company.getId());
            companyRes.setCompanyName(company.getCompanyName());
            companyRes.setCompanyLogoContent(company.getCompanyLogo().getFileContent());
            companyRes.setCompanyLogoExtension(company.getCompanyLogo().getFileExtension());

            companiesRes.add(companyRes);
        });

        return companiesRes;
    }

    @Override
    @Transactional
    public UpdateResDto updateCompany(UpdateCompanyReqDto data) {
        final var updateRes = new UpdateResDto();

        var isUpdateFileOnly = true;

        final var companyId = data.getId();
        final var companyName = data.getCompanyName();

        var company = companyRepository.findById(companyId).get();

        if (!company.getCompanyName().toLowerCase().equals(companyName.toLowerCase())) {
            final var resultName = companyRepository.getCompanyNameByIdAndName(companyId, companyName);

            if (resultName.isEmpty()) {
                company.setCompanyName(companyName);
                company.setUpdatedBy(principalService.getUserId());

                isUpdateFileOnly = false;
            } else {
                throw new FailCheckException("Company name already existed", HttpStatus.BAD_REQUEST);
            }
        }

        final var companyLogoContent = data.getCompanyLogoContent();
        var file = company.getCompanyLogo();

        if (!file.getFileContent().equals(companyLogoContent)) {
            file.setFileContent(companyLogoContent);
            file.setFileExtension(data.getCompanyLogoExtension());

            file = fileService.updateFile(file);

            company.setCompanyLogo(file);
        }

        if (!isUpdateFileOnly) {
            company = companyRepository.save(company);
            updateRes.setVersion(company.getVer());
        }

        updateRes.setMessage("Company data has been updated!");

        return updateRes;
    }

}
