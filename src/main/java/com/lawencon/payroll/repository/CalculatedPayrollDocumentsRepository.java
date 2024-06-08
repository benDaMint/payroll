package com.lawencon.payroll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawencon.payroll.model.CalculatedPayrollDocuments;

public interface CalculatedPayrollDocumentsRepository extends JpaRepository<CalculatedPayrollDocuments, String> {
  List<CalculatedPayrollDocuments> findAllByScheduleId(String scheduleId);
}
