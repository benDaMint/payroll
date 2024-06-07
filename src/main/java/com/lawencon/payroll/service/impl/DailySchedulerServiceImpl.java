package com.lawencon.payroll.service.impl;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.lawencon.payroll.constant.NotificationCodes;
import com.lawencon.payroll.constant.Roles;
import com.lawencon.payroll.constant.ScheduleRequestTypes;
import com.lawencon.payroll.model.Notification;
import com.lawencon.payroll.model.Schedule;
import com.lawencon.payroll.repository.ClientAssignmentRepository;
import com.lawencon.payroll.repository.DocumentRepository;
import com.lawencon.payroll.repository.NotificationRepository;
import com.lawencon.payroll.repository.NotificationTemplateRepository;
import com.lawencon.payroll.repository.ScheduleRepository;
import com.lawencon.payroll.repository.ScheduleRequestTypeRepository;
import com.lawencon.payroll.repository.UserRepository;
import com.lawencon.payroll.service.DailySchedulerService;
import com.lawencon.payroll.service.EmailService;
import com.lawencon.payroll.util.FtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DailySchedulerServiceImpl implements DailySchedulerService {

  @Autowired
  private EmailService emailService;

  private final ScheduleRepository scheduleRepository;
  private final ClientAssignmentRepository clientAssignmentRepository;
  private final UserRepository userRepository;
  private final ScheduleRequestTypeRepository scheduleRequestTypeRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationTemplateRepository notificationTemplateRepository;
  private final DocumentRepository documentRepository;

  @Scheduled(fixedRate = 1000 * 60 * 30)
  @Override
  public void addMonthlyScheduleJob() {
    final var currentTime = LocalDateTime.now().getHour();

    if (currentTime >= 0 && currentTime < 1) {
      final var clientAssignments = clientAssignmentRepository.findAll();
      final var system = userRepository.findByRoleIdRoleCode(Roles.RL000.name());
      final var scheduleRequestType = scheduleRequestTypeRepository
          .findByScheduleRequestCode(ScheduleRequestTypes.SQT01.name());
      final var notificationTemplate = notificationTemplateRepository
          .findByNotificationCode(NotificationCodes.NT001.name());
      final var deadlineNotificationTemplate = notificationTemplateRepository
          .findByNotificationCode(NotificationCodes.NT005.name());

      clientAssignments.forEach(clientAssignment -> {
        final var latestSchedule = scheduleRepository
            .findFirstByClientAssignmentIdOrderByCreatedAtDesc(clientAssignment.getId());

        if (latestSchedule.isEmpty()
            || latestSchedule.get().getCreatedAt().getMonthValue() < LocalDateTime.now().getMonthValue()) {
          final var user = clientAssignment.getPsId();
          var schedule = new Schedule();

          schedule.setCreatedBy(system.getId());
          schedule.setClientAssignment(clientAssignment);
          schedule.setScheduleRequestType(scheduleRequestType);

          final var current = LocalDateTime.now();

          final var month = current.getMonth() + "-" + current.getYear();

          final String nestedDirectory = clientAssignment.getClientId().getId() + "/" + month + "/";

          FtpUtil.createNestedDirectory(nestedDirectory);

          final String finalDocuments = nestedDirectory + "finalDocuments/";

          FtpUtil.createNestedDirectory(finalDocuments);

          schedule = scheduleRepository.save(schedule);

          final String scheduleId = schedule.getId();

          final String payrollDate = schedule.getCreatedAt().toString();

          final var routeLink = "schedules/create?id=" + scheduleId + "&payrollDate=" + payrollDate;

          final var notification = new Notification();
          notification.setNotificationTemplate(notificationTemplate);
          notification.setCreatedBy(system.getId());
          notification.setRouteLink(routeLink);

          notification.setUser(user);

          notificationRepository.save(notification);

          System.out.println("Payroll Schedule Created!");

          final var email = user.getEmail();

          final var subject = "New Schedule has been Created[" + user.getCompanyId().getPayrollDate() + "/"
              + LocalDateTime.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "/"
              + LocalDateTime.now().getYear() + " ]";

          Map<String, Object> templateBody = new HashMap<>();
          templateBody.put("subject", subject);
          templateBody.put("username", user.getUserName());
          templateBody.put("message",
              "The new schedule has been successfully created by the system. Please check the new schedule and set the schedule deadline soon");

          final Runnable runnable = () -> {
            try {
              emailService.sendEmail(email, subject, templateBody);
            } catch (MessagingException e) {
              e.printStackTrace();
            }
          };

          final var mailThread = new Thread(runnable);
          mailThread.start();
        } else {
          final var user = clientAssignment.getClientId();

          final var documents = documentRepository.findAll();
          final var currentDate = LocalDateTime.now().getDayOfMonth();
          documents.forEach(document -> {
            if (Optional.ofNullable(document.getDocumentDirectory()).isEmpty()
                && currentDate + 2 >= document.getDocumentDeadline().getDayOfMonth()) {
              final var routeLink = "payrolls/" + document.getSchedule().getId();

              final var notification = new Notification();
              notification.setNotificationTemplate(deadlineNotificationTemplate);
              notification.setCreatedBy(system.getId());
              notification.setRouteLink(routeLink);

              notification.setUser(clientAssignment.getClientId());

              notificationRepository.save(notification);

            }
          });
          final var email = user.getEmail();

          final var subject = "Urgent Reminder: Upcoming Payroll Deadline";

          Map<String, Object> templateBody = new HashMap<>();
          templateBody.put("subject", subject);
          templateBody.put("username", user.getUserName());
          templateBody.put("message",
              "This is a friendly reminder that the deadline for submitting your payroll documents is approaching. To ensure timely processing of your payroll, please submit all necessary documents. Thank you!");

          final Runnable runnable = () -> {
            try {
              emailService.sendEmail(email, subject, templateBody);
            } catch (MessagingException e) {
              e.printStackTrace();
            }
          };

          final var mailThread = new Thread(runnable);
          mailThread.start();
        }
      });

    }
  }
}