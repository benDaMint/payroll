package com.lawencon.payroll.dto.document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateCalculatedDocumentsReqDto {
  private String documentId;
  private String base64;
  private String documentName;
}
