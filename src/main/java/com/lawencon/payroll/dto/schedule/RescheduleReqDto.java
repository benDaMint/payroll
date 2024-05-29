package com.lawencon.payroll.dto.schedule;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RescheduleReqDto {
  private String scheduleId;
  private String newDeadline;
}
