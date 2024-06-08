package com.lawencon.payroll.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.payroll.dto.company.CompanyResDto;
import com.lawencon.payroll.dto.company.UpdateCompanyReqDto;
import com.lawencon.payroll.dto.generalResponse.UpdateResDto;
import com.lawencon.payroll.service.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("")
    public ResponseEntity<List<CompanyResDto>> getCompanies() {
        final var companiesRes = companyService.getCompanies();
        return new ResponseEntity<>(companiesRes, HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<UpdateResDto> updateCompany(@RequestBody UpdateCompanyReqDto data) {
        final var updateRes = companyService.updateCompany(data);
        return new ResponseEntity<>(updateRes, HttpStatus.OK);
    }
}
