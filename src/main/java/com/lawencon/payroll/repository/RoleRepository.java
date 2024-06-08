package com.lawencon.payroll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lawencon.payroll.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>
{
    Role findByRoleCode(String code);

    @Query(value = "SELECT ro FROM Role ro "
                + "WHERE ro.roleCode NOT IN "
                + "(:code1, :code2) ")
    List<Role> findByRoleCodeNotIn(String code1, String code2);
}