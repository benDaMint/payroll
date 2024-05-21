package com.lawencon.payroll.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lawencon.payroll.constant.NotificationCodes;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.dto.payroll.PayrollResDto;
import com.lawencon.payroll.model.Notification;
import com.lawencon.payroll.repository.ClientAssignmentRepository;
import com.lawencon.payroll.repository.NotificationRepository;
import com.lawencon.payroll.repository.NotificationTemplateRepository;
import com.lawencon.payroll.repository.ScheduleRepository;
import com.lawencon.payroll.repository.UserRepository;
import com.lawencon.payroll.service.PayrollService;
import com.lawencon.payroll.service.PrincipalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {
  
  private final PrincipalService principalService;
  private final ClientAssignmentRepository clientAssignmentRepository;
  private final NotificationTemplateRepository notificationTemplateRepository;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final ScheduleRepository scheduleRepository;

  @Override
  public List<PayrollResDto> getClientPayrolls() {
      final var psId = principalService.getUserId();
      final var payrollRes = new ArrayList<PayrollResDto>();
      final var clientAssignments = clientAssignmentRepository.getByPsId(psId);

      clientAssignments.forEach(clientAssignment -> {
          final var payroll = new PayrollResDto();
          
          final var clientAssignmentId = clientAssignment.getId();
          final var schedule = scheduleRepository.findFirstByClientAssignmentIdOrderByCreatedAtDesc(clientAssignmentId);
          final var clientName = clientAssignment.getClientId().getUserName();
          final var payrollDate = clientAssignment.getClientId().getCompanyId().getPayrollDate();
          final var scheduleStatus = schedule.getScheduleRequestType().getScheduleRequestName();

          payroll.setId(clientAssignmentId);
          payroll.setClientName(clientName);
          payroll.setPayrollDate(payrollDate);
          payroll.setScheduleStatus(scheduleStatus);

          payrollRes.add(payroll);
      });

      return payrollRes;
  }

  @Override
  public InsertResDto createPingNotification(String clientId) {
    final var insertRes = new InsertResDto();

    var notification = new Notification();

    final var user = userRepository.findById(clientId);

    final var notificationTemplate = notificationTemplateRepository.findByNotificationCode(NotificationCodes.NT003.name());

    notification.setNotificationTemplate(notificationTemplate);
    notification.setUser(user.get());
    notification.setCreatedBy(principalService.getUserId());

    notification = notificationRepository.save(notification);

    insertRes.setId(notification.getId());
    insertRes.setMessage("Client Has Been Pinged");

    return insertRes;
  }
}
