  package com.lawencon.payroll.constant;

  import lombok.Getter;

  @Getter
  public enum NotificationCodes {
    NT001("New Monthly Payroll Has Been Created"), 
    NT002("New Payroll Schedule Has Been Created"), 
    NT003("New Client Document(s) Has Been Uploaded"), 
    NT004("Your Document(s) Has Been Accepted"), 
    NT005("Document(s) Required"), 
    NT006("Calculated Payroll Document(s) Has Been Uploaded"),
    NT007("New Client Reschedule Request");

    private final String code;

    NotificationCodes(String code) {
      this.code = code;
    }
  }
