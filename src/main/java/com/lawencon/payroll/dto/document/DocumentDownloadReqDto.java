package com.lawencon.payroll.dto.document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentDownloadReqDto {
  private String remoteDocument;
  private String downloadDocument;
}
