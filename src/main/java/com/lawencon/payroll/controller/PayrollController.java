package com.lawencon.payroll.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.dto.notification.NotificationResDto;
import com.lawencon.payroll.dto.payroll.PayrollResDto;
import com.lawencon.payroll.dto.schedule.ScheduleResDto;
import com.lawencon.payroll.service.PayrollService;
import com.lawencon.payroll.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("payrolls")
public class PayrollController {
  private final PayrollService payrollService;
  private final ScheduleService scheduleService;
  
  @GetMapping("clients")
  public ResponseEntity<List<PayrollResDto>> getPayroll() {
    final var payrollRes = payrollService.getClientPayrolls();

    return new ResponseEntity<>(payrollRes, HttpStatus.OK);
  }

  @GetMapping("{clientAssignmentId}")
  public ResponseEntity<List<ScheduleResDto>> getClientSchedule(
    @PathVariable String clientAssignmentId) {
      final var scheduleRes = scheduleService.getByClientAssignmentId(clientAssignmentId);

      return new ResponseEntity<>(scheduleRes, HttpStatus.OK);
  }

  @PostMapping("ping/{scheduleId}")
  public ResponseEntity<InsertResDto> pingClient(@PathVariable String scheduleId) {
    final var InsertResDto = payrollService.createPingNotification(scheduleId);

    return new ResponseEntity<>(InsertResDto, HttpStatus.OK);
  }

  @GetMapping("notifications")
  public ResponseEntity<List<NotificationResDto>> getNotification() {
    final var notificationRes = payrollService.getNotification();

    return new ResponseEntity<>(notificationRes, HttpStatus.OK);
  }
}
