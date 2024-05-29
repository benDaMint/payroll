package com.lawencon.payroll.service;

import java.util.List;

import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.dto.notification.NotificationResDto;
import com.lawencon.payroll.dto.payroll.PayrollResDto;
import com.lawencon.payroll.dto.schedule.RescheduleReqDto;

public interface PayrollService {
  List<PayrollResDto> getClientPayrolls();

  InsertResDto createPingNotification(String clientAssignmentId);

  List<NotificationResDto> getNotification();

  InsertResDto createRescheduleNotification(RescheduleReqDto data);
}
