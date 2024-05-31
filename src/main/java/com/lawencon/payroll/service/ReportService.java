package com.lawencon.payroll.service;

import java.io.FileNotFoundException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface ReportService {
  JasperPrint exportReport(String scheduleId) throws FileNotFoundException, JRException;
}