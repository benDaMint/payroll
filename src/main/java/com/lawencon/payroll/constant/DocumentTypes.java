package com.lawencon.payroll.constant;

import lombok.Getter;
@Getter
public enum DocumentTypes {
  DT001("Cut Off Attendance"), DT002("Employee Data Changes"), DT003("Payroll Data"), DT004("Optional");

  private final String documentTypeName;

  DocumentTypes(String documentTypeName) {
    this.documentTypeName = documentTypeName;
  }
}