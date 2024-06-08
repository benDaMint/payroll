package com.lawencon.payroll.dto.schedule;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScheduleReqDto {
  private String clientAssignmentId;
  private String ScheduleRequestTypeId;
  private Float payrollDate;
}