package com.lawencon.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.payroll.model.ScheduleRequestType;


@Repository
public interface ScheduleRequestTypeRepository extends JpaRepository<ScheduleRequestType, String>{
  ScheduleRequestType findByScheduleRequestCode(String code);
}