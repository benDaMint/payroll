package com.lawencon.payroll.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lawencon.payroll.constant.NotificationCodes;
import com.lawencon.payroll.constant.ScheduleRequestTypes;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.dto.notification.NotificationResDto;
import com.lawencon.payroll.dto.payroll.PayrollResDto;
import com.lawencon.payroll.model.Notification;
import com.lawencon.payroll.repository.ClientAssignmentRepository;
import com.lawencon.payroll.repository.NotificationRepository;
import com.lawencon.payroll.repository.NotificationTemplateRepository;
import com.lawencon.payroll.repository.ScheduleRepository;
import com.lawencon.payroll.repository.ScheduleRequestTypeRepository;
import com.lawencon.payroll.repository.UserRepository;
import com.lawencon.payroll.service.EmailService;
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
  private final EmailService emailService;
  private final ScheduleRequestTypeRepository scheduleRequestTypeRepository;

  @Override
  public List<PayrollResDto> getClientPayrolls() {
      final var psId = principalService.getUserId();
      final var payrollRes = new ArrayList<PayrollResDto>();
      final var clientAssignments = clientAssignmentRepository.getByPsId(psId);

      clientAssignments.forEach(clientAssignment -> {
          final var clientAssignmentId = clientAssignment.getId();
          final var schedule = scheduleRepository.findFirstByClientAssignmentIdOrderByCreatedAtDesc(clientAssignmentId);
          
          final var payroll = new PayrollResDto();
          final var clientName = clientAssignment.getClientId().getUserName();
          final var payrollDate = clientAssignment.getClientId().getCompanyId().getPayrollDate();

          payroll.setClientAssignmentId(clientAssignmentId);
          payroll.setClientName(clientName);

          if(schedule.isPresent()) {
            final var scheduleStatusName = schedule.get().getScheduleRequestType().getScheduleRequestName();
            final var scheduleStatusCode = schedule.get().getScheduleRequestType().getScheduleRequestCode();
            final var monthYearFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
            final var createdAt = monthYearFormatter.format(schedule.get().getCreatedAt());
  
            final var returnedPayrollDate = payrollDate+"/"+createdAt;
  
            payroll.setPayrollDate(returnedPayrollDate);
            payroll.setScheduleStatusName(scheduleStatusName);
            payroll.setScheduleStatusCode(scheduleStatusCode);
          }else {
            final var scheduleStatus = scheduleRequestTypeRepository.findByScheduleRequestCode(ScheduleRequestTypes.SQT00.name());
            payroll.setScheduleStatusName(scheduleStatus.getScheduleRequestName());
            payroll.setScheduleStatusCode(scheduleStatus.getScheduleRequestCode());
            payroll.setPayrollDate("--/--/--");
          }

          payrollRes.add(payroll);
      });

      return payrollRes;
  }

  @Override
  public InsertResDto createPingNotification(String clientAssignmentId) {
    final var insertRes = new InsertResDto();

    var notification = new Notification();

    final var clientAssignment = clientAssignmentRepository.findById(clientAssignmentId);

    final var clientId = clientAssignment.get().getClientId().getId();

    final var user = userRepository.findById(clientId);

    final var notificationTemplate = notificationTemplateRepository.findByNotificationCode(NotificationCodes.NT003.name());

    notification.setNotificationTemplate(notificationTemplate);
    notification.setUser(user.get());
    notification.setCreatedBy(principalService.getUserId());

    notification = notificationRepository.save(notification);

    final var email = user.get().getEmail();

    final var subject = "New User Information";

    final var body = "Hello" + user.get().getUserName() + "!\n"
                   + "You Need To Send The Required Documents For The Payroll Service System!";

    final Runnable runnable = () -> {
      emailService.sendEmail(email, subject, body);
    };

    final var mailThread = new Thread(runnable);
    mailThread.start();

    insertRes.setId(notification.getId());
    insertRes.setMessage("Client Has Been Pinged");

    return insertRes;
  }

  @Override
  public List<NotificationResDto> getNotification() {
    final var notificationsRes = new ArrayList<NotificationResDto>();
    final var userId = principalService.getUserId();
    final var notifications = notificationRepository.findAllByUserId(userId);

    notifications.forEach(notification -> {
      final var notificationRes = new NotificationResDto();

      final var id = notification.getId();
      final var code = notification.getNotificationTemplate().getNotificationCode();
      final var header = notification.getNotificationTemplate().getNotificationHeader();
      final var body = notification.getNotificationTemplate().getNotificationBody();

      notificationRes.setNotificationId(id);
      notificationRes.setNotificationBody(code);
      notificationRes.setNotificationCode(header);
      notificationRes.setNotificationHeader(body);

      notificationsRes.add(notificationRes);
    });

    return notificationsRes;
  }
}

