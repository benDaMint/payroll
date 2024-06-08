package com.lawencon.payroll.dto.document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentsResDto {
  private String documentId;
  private String documentName;
  private String documentDirectory;
  private String activity;
  private String documentDeadline;
  private Boolean isSignedByClient;
  private Boolean isSignedByPs;
}
