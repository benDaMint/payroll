package com.lawencon.payroll.dto.document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateDocumentReqDto {
  private String documentId;
  private String base64;
  private String documentName;
  private String scheduleId;
  private String clientAssignmentId;
  private Boolean isSignedByClient;
  private Boolean isSignedByPS;
}
