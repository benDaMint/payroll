package com.lawencon.payroll.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lawencon.payroll.service.PrincipalService;

@Service
public class PrincipalServiceImpl implements PrincipalService {

    @Override
    public String getUserId() {
        final var auth = SecurityContextHolder.getContext().getAuthentication();
		final var userAuth = auth.getPrincipal();
		return userAuth.toString();
    }

}
