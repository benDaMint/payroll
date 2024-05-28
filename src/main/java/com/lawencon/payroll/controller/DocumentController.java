package com.lawencon.payroll.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.payroll.dto.document.DocumentReqDto;
import com.lawencon.payroll.dto.document.DocumentResDto;
import com.lawencon.payroll.dto.document.OldDocumentResDto;
import com.lawencon.payroll.dto.document.UpdateCalculatedDocumentReqDto;
import com.lawencon.payroll.dto.document.UpdateDocumentReqDto;
import com.lawencon.payroll.dto.document.UpdateDocumentScheduleReqDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.dto.generalResponse.UpdateResDto;
import com.lawencon.payroll.service.DocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping()
    public ResponseEntity<InsertResDto> createDocumentsSchedule(@RequestBody DocumentReqDto data) {
        final var insertRes = documentService.createDocuments(data);
        return new ResponseEntity<>(insertRes, HttpStatus.CREATED);
    }

    @PatchMapping()
    public ResponseEntity<UpdateResDto> uploadDocument(@RequestBody UpdateDocumentReqDto data) {
        final var updateRes = documentService.uploadDocument(data);

        return new ResponseEntity<>(updateRes, HttpStatus.OK); 
    }

    @GetMapping("download/{id}")
    public ResponseEntity<?> downloadDocument(@PathVariable String id) {
        final var downloadRes = documentService.downloadDocument(id);
        
        final String fileName = downloadRes.getFileName();
        
        final byte[] fileBytes = downloadRes.getFileBytes();
		
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=" + fileName).body(fileBytes);
    }
    
    @GetMapping("final/download/{id}")
    public ResponseEntity<?> downloadFinalDocument(@PathVariable String id) {
        final var downloadRes = documentService.downloadFinalDocument(id);
        
        final String fileName = downloadRes.getFileName();
        
        final byte[] fileBytes = downloadRes.getFileBytes();
		
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=" + fileName).body(fileBytes);
    }

    @GetMapping("{scheduleId}")
    public ResponseEntity<DocumentResDto> getDocumentSchedule(@PathVariable String scheduleId) {
        final var documentRes = documentService.getDocumentsByScheduleId(scheduleId);

        return new ResponseEntity<>(documentRes, HttpStatus.OK);
    }

    @GetMapping("original/{scheduleId}")
    public ResponseEntity<List<OldDocumentResDto>> getOldDocumentsData(@PathVariable String scheduleId) {
        final var oldDocumentsRes = documentService.getOldDocuments(scheduleId);

        return new ResponseEntity<>(oldDocumentsRes, HttpStatus.OK);
    }

    @PatchMapping("schedule")
    public ResponseEntity<UpdateResDto> rescheduleDocuments(@RequestBody List<UpdateDocumentScheduleReqDto> data) {
        final var updateRes = documentService.rescheduleDocuments(data);

        return new ResponseEntity<>(updateRes, HttpStatus.OK);
    }

    @PatchMapping("final")
    public ResponseEntity<UpdateResDto> uploadFinalDocuments(@RequestBody UpdateCalculatedDocumentReqDto data) {

        final var updateRes = documentService.uploadFinalDocument(data);

        return new ResponseEntity<>(updateRes, HttpStatus.OK);
    }
}
