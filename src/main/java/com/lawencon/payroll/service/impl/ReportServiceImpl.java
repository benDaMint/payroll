package com.lawencon.payroll.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.lawencon.payroll.dto.schedule.ScheduleResDto;
import com.lawencon.payroll.service.ReportService;
import com.lawencon.payroll.service.ScheduleService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService  {
  private final ScheduleService scheduleService;

  @Override
  public JasperPrint exportReport() throws FileNotFoundException, JRException {
    final List<ScheduleResDto> schedules = scheduleService.getByLoginClient();

		final File file = ResourceUtils.getFile("classpath:PayRollScheduleReport.jasper");
		
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(schedules);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "PSS");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(file.getAbsolutePath(), parameters, dataSource);

		return jasperPrint;
  }
}