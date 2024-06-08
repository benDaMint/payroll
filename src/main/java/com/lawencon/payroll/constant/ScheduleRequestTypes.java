package com.lawencon.payroll.constant;

import lombok.Getter;

@Getter
public enum ScheduleRequestTypes {
  SQT00("No Schedule"), SQT01("Pending Schedule"), SQT02("Pending Client Document"), SQT03("Pending Feedback"), SQT04("Completed");

  private final String typeName;

  ScheduleRequestTypes(String typeName) {
    this.typeName = typeName;
  }
}