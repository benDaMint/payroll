package com.lawencon.payroll.service;

import java.util.List;

import com.lawencon.payroll.dto.document.DocumentDownloadResDto;
import com.lawencon.payroll.dto.document.DocumentReqDto;
import com.lawencon.payroll.dto.document.DocumentResDto;
import com.lawencon.payroll.dto.document.OldDocumentResDto;
import com.lawencon.payroll.dto.document.UpdateCalculatedDocumentReqDto;
import com.lawencon.payroll.dto.document.UpdateDocumentReqDto;
import com.lawencon.payroll.dto.document.UpdateDocumentScheduleReqDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.dto.generalResponse.UpdateResDto;

public interface DocumentService {
    InsertResDto createDocuments(DocumentReqDto data);

    UpdateResDto rescheduleDocuments(List<UpdateDocumentScheduleReqDto> data);

    UpdateResDto uploadFinalDocument(UpdateCalculatedDocumentReqDto data);

    UpdateResDto uploadDocument(UpdateDocumentReqDto data);

    DocumentDownloadResDto downloadDocument(String id);
    DocumentDownloadResDto downloadFinalDocument(String id);

    DocumentResDto getDocumentsByScheduleId(String data);

    List<OldDocumentResDto> getOldDocuments(String scheduleId);
}
