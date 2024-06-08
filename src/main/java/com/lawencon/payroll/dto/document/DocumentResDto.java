package com.lawencon.payroll.dto.document;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentResDto {
  private String clientAssignmentId;
  private List<DocumentsResDto> documentsRes;
  private List<CalculatedResDto> calculatedDataResDto;
}
