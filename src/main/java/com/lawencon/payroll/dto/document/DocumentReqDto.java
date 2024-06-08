package com.lawencon.payroll.dto.document;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentReqDto {
    private String scheduleId;
    private List<DocumentsReqDto> documentsReqDto;
}
