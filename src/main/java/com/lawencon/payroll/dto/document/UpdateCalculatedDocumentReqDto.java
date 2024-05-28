package com.lawencon.payroll.dto.document;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateCalculatedDocumentReqDto {
  private String scheduleId;
  private String clientAssignmentId;
  private List<UpdateCalculatedDocumentsReqDto> documents;
}
