package com.lawencon.payroll.service.impl;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.lawencon.payroll.constant.NotificationCodes;
import com.lawencon.payroll.constant.Roles;
import com.lawencon.payroll.constant.ScheduleRequestTypes;
import com.lawencon.payroll.model.Notification;
import com.lawencon.payroll.model.Schedule;
import com.lawencon.payroll.repository.ClientAssignmentRepository;
import com.lawencon.payroll.repository.NotificationRepository;
import com.lawencon.payroll.repository.NotificationTemplateRepository;
import com.lawencon.payroll.repository.ScheduleRepository;
import com.lawencon.payroll.repository.ScheduleRequestTypeRepository;
import com.lawencon.payroll.repository.UserRepository;
import com.lawencon.payroll.service.DailySchedulerService;
import com.lawencon.payroll.util.FtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DailySchedulerServiceImpl implements DailySchedulerService {
  
  private final ScheduleRepository scheduleRepository;
  private final ClientAssignmentRepository clientAssignmentRepository;
  private final UserRepository userRepository;
  private final ScheduleRequestTypeRepository scheduleRequestTypeRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationTemplateRepository notificationTemplateRepository;

  @Scheduled(fixedRate = 1000 * 60 * 30)
  @Override
  public void addMonthlyScheduleJob() {
    final var currentTime = LocalDateTime.now().getHour();
    
    if(currentTime >= 0 && currentTime <= 1 ){
      final var schedule = new Schedule();
      final var clientAssignments = clientAssignmentRepository.findAll();
      final var system = userRepository.findByRoleIdRoleCode(Roles.RL000.name());
      final var scheduleRequestType = scheduleRequestTypeRepository.findByScheduleRequestCode(ScheduleRequestTypes.SQT01.name());
      final var notificationTemplate = notificationTemplateRepository.findByNotificationCode(NotificationCodes.NT001.name());
      
      final var notification = new Notification();
      notification.setNotificationTemplate(notificationTemplate);
      notification.setCreatedBy(system.getId());
      
      clientAssignments.forEach(clientAssignment -> {
        final var latestSchedule = scheduleRepository.findFirstByClientAssignmentIdOrderByCreatedAtDesc(clientAssignment.getId());
        
        if(latestSchedule == null || latestSchedule.getCreatedAt().getMonthValue() < LocalDateTime.now().getMonthValue()) {
          schedule.setCreatedBy(system.getId());
          schedule.setClientAssignment(clientAssignment);
          schedule.setScheduleRequestType(scheduleRequestType);
          
          notification.setUser(clientAssignment.getPsId());

          FtpUtil.createNestedDirectory(clientAssignment.getClientId().getId());

          scheduleRepository.save(schedule);
          notificationRepository.save(notification);

          System.out.println("Payroll Schedule Created!");
        }
      });
    }
  }
}