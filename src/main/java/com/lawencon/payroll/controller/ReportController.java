package com.lawencon.payroll.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.payroll.service.ReportService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;

@RestController
@RequiredArgsConstructor
@RequestMapping("reports")
public class ReportController {
  private final ReportService reportService;

  @GetMapping()
  public ResponseEntity<?> downloadFinalDocument() throws FileNotFoundException, JRException  {
      final String fileName = "Report.pdf";
      
      final var jasPrint = reportService.exportReport();

      final var fileArray = JasperExportManager.exportReportToPdf(jasPrint);

      return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
      "attachment; filename=" + fileName).body(fileArray);
  }
}
