package com.lawencon.payroll.dto.document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CalculatedResDto {
  private String documentId;
  private String documentName;
  private String documentDirectory;
  private String activity;
}
