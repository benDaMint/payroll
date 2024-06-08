package com.lawencon.payroll.service;

import java.util.List;

import com.lawencon.payroll.dto.role.RoleResDto;
import com.lawencon.payroll.model.Role;

public interface RoleService {
    Role getIdByRole(String code);

    List<RoleResDto> getRolesExcept();

    Role getById(String id);
}
