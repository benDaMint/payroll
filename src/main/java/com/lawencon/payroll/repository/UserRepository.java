package com.lawencon.payroll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lawencon.payroll.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
  User findByEmail(String email);

  @Query(value = "SELECT cl FROM User cl "
              + "INNER JOIN Role r "
              + "ON cl.roleId = r.id "
              + "WHERE r.roleCode = :roleCode ")
  List<User> findByRoleRoleCode(@Param("roleCode") String roleCode);
  
  @Query(value = "SELECT cl FROM User cl "
              + "INNER JOIN Role ro "
              + "ON cl.roleId.id = ro.id "
              + "WHERE ro.roleCode = :roleCode "
              + "AND cl.id IN "
              + "( "
              + "SELECT ca.clientId FROM ClientAssignment ca "
              + "WHERE ca.psId.id = :psId "
              + ") ")
  List<User> findAllByRoleCodeAndId(@Param("roleCode") String roleCode, @Param("psId") String psId);

  @Query(value = "SELECT cl FROM User cl "
              + "INNER JOIN Role ro "
              + "ON cl.roleId.id = ro.id "
              + "WHERE ro.roleCode = :roleCode "
              + "AND cl.id NOT IN "
              + "( "
              + "SELECT ca.clientId FROM ClientAssignment ca "
              + "WHERE ca.psId.id = :psId "
              + ") ")
  List<User> findAllByRoleCodeAndIdNot(@Param("roleCode") String roleCode, @Param("psId") String psId);

  @Query(value ="SELECT us FROM User us "
              + "WHERE us.isActive = TRUE ")
  List<User> findAll();
  
  User findByRoleIdRoleCode(String roleCode);
}