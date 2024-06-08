package com.lawencon.payroll.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.payroll.dto.user.LoginReqDto;
import com.lawencon.payroll.dto.user.LoginResDto;
import com.lawencon.payroll.service.LoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("")
    public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto data) {
        final var loginRes = loginService.loginUser(data);
        return new ResponseEntity<>(loginRes, HttpStatus.OK);
    }
}
