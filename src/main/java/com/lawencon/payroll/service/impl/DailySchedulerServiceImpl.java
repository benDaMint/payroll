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
    
    if(currentTime > 0 && currentTime < 1 ){
      final var clientAssignments = clientAssignmentRepository.findAll();
      final var system = userRepository.findByRoleIdRoleCode(Roles.RL000.name());
      final var scheduleRequestType = scheduleRequestTypeRepository.findByScheduleRequestCode(ScheduleRequestTypes.SQT01.name());
      final var notificationTemplate = notificationTemplateRepository.findByNotificationCode(NotificationCodes.NT001.name());

      
      clientAssignments.forEach(clientAssignment -> {
        final var latestSchedule = scheduleRepository.findFirstByClientAssignmentIdOrderByCreatedAtDesc(clientAssignment.getId());
        
        if(latestSchedule.isEmpty() || latestSchedule.get().getCreatedAt().getMonthValue() < LocalDateTime.now().getMonthValue()) {
          var schedule = new Schedule();
          
          schedule.setCreatedBy(system.getId());
          schedule.setClientAssignment(clientAssignment);
          schedule.setScheduleRequestType(scheduleRequestType);

			    final var current = LocalDateTime.now();

			    final var month = current.getMonth() + "-" + current.getYear();

			    final String nestedDirectory = clientAssignment.getClientId().getId() +"/"+ month + "/";

          FtpUtil.createNestedDirectory(nestedDirectory);

          final String finalDocuments = nestedDirectory + "finalDocuments/";

          FtpUtil.createNestedDirectory(finalDocuments);

          schedule = scheduleRepository.save(schedule);
          
          final String scheduleId = schedule.getId();

          final String payrollDate = schedule.getCreatedAt().toString();

          final var routeLink = "schedules/create?id="+scheduleId+"&payrollDate="+payrollDate;
      
          final var notification = new Notification();
          notification.setNotificationTemplate(notificationTemplate);
          notification.setCreatedBy(system.getId()); 
          notification.setRouteLink(routeLink);
          
          notification.setUser(clientAssignment.getPsId());

          notificationRepository.save(notification);

          System.out.println("Payroll Schedule Created!");
        }
      });
    }
  }
}