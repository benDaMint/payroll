package com.lawencon.payroll.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.payroll.dto.role.RoleResDto;
import com.lawencon.payroll.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("")
    public ResponseEntity<List<RoleResDto>> getUserCreationRoles() {
        final var roles = roleService.getRolesExcept();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
