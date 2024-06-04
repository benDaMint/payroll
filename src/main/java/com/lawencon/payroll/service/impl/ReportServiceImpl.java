package com.lawencon.payroll.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.lawencon.payroll.model.Document;
import com.lawencon.payroll.repository.DocumentRepository;
import com.lawencon.payroll.service.ReportService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService  {
  private final DocumentRepository documentRepository;

  @Override
  public JasperPrint exportReport(String scheduleId) throws FileNotFoundException, JRException {
    final List<Document> documents = documentRepository.findByScheduleId(scheduleId);

		final File file = ResourceUtils.getFile("classpath:PayRollScheduleReport.jrxml");

		final JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(documents);

		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "Josep Gultom");
		
		final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

		return jasperPrint;
  }
}