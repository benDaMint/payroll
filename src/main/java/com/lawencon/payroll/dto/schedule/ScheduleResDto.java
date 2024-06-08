package com.lawencon.payroll.dto.schedule;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScheduleResDto {
  private String scheduleId;
  private String scheduleStatusName;
  private String scheduleStatusCode;
  private String payrollDate;
  private Boolean canBeRescheduled;
}