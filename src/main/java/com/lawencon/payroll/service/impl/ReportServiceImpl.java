package com.lawencon.payroll.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.lawencon.payroll.dto.report.ReportResDto;
import com.lawencon.payroll.model.Document;
import com.lawencon.payroll.repository.CalculatedPayrollDocumentsRepository;
import com.lawencon.payroll.repository.ClientAssignmentRepository;
import com.lawencon.payroll.repository.DocumentRepository;
import com.lawencon.payroll.repository.ScheduleRepository;
import com.lawencon.payroll.repository.UserRepository;
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
public class ReportServiceImpl implements ReportService {
	private final DocumentRepository documentRepository;
	private final CalculatedPayrollDocumentsRepository calculatedPayrollDocumentsRepository;
	private final ScheduleRepository scheduleRepository;
	private final ClientAssignmentRepository clientAssignmentRepository;
	private final UserRepository userRepository;

	@Override
	public JasperPrint exportReport(String scheduleId) throws FileNotFoundException, JRException {
		final List<Document> documents = documentRepository.findByScheduleId(scheduleId);

		final var calculatedSalaryDoc = calculatedPayrollDocumentsRepository.findAllByScheduleId(scheduleId);

		final List<ReportResDto> reports = new ArrayList<>();

		for (int i = 0; i < calculatedSalaryDoc.size(); i++) {
			final var calculatedDoc = new Document();
			calculatedDoc.setActivity(calculatedSalaryDoc.get(i).getActivity());
			calculatedDoc.setDocumentName(calculatedSalaryDoc.get(i).getDocumentName());
			calculatedDoc.setDocumentDeadline(calculatedSalaryDoc.get(i).getUpdatedAt());
			calculatedDoc.setUpdatedAt(calculatedSalaryDoc.get(i).getUpdatedAt());

			documents.add(calculatedDoc);
		}

		for (int i = 0; i < documents.size(); i++) {
			final var report = new ReportResDto();
			report.setId(documents.get(i).getId());
			report.setActivity(documents.get(i).getActivity());
			report.setDocumentName(documents.get(i).getDocumentName());
			report.setDocumentDeadline(documents.get(i).getDocumentDeadline());
			report.setUpdatedAt(documents.get(i).getUpdatedAt());

			final var deadline = report.getDocumentDeadline();
			final var accepted = report.getUpdatedAt();

			if (deadline.getMonthValue() <= accepted.getMonthValue()
					&& deadline.getDayOfMonth() <= accepted.getDayOfMonth()) {
				report.setInformation("On Time");
			} else {
				report.setInformation("Late");
			}

			reports.add(report);
		}

		final var schedule = scheduleRepository.findById(scheduleId);

		final var clientAssignment = clientAssignmentRepository.findById(schedule.get().getClientAssignment().getId());

		final var client = userRepository.findById(clientAssignment.get().getClientId().getId());

		final File file = ResourceUtils.getFile("classpath:PayRollScheduleReport.jrxml");

		final JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reports);

		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("payrollDate", schedule.get().getCreatedAt());
		parameters.put("clientName", client.get().getUserName());
		parameters.put("companyName", client.get().getCompanyId().getCompanyName());

		final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

		return jasperPrint;
	}
}