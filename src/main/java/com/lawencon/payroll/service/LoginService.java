package com.lawencon.payroll.service;

import com.lawencon.payroll.dto.user.LoginReqDto;
import com.lawencon.payroll.dto.user.LoginResDto;

public interface LoginService {
    LoginResDto loginUser(LoginReqDto data);
}
