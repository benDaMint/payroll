package com.lawencon.payroll.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.payroll.dto.generalResponse.InsertResDto;

import java.util.Base64;
import java.util.Optional;

import com.lawencon.payroll.dto.file.FileReqDto;
import com.lawencon.payroll.model.File;
import com.lawencon.payroll.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("{id}")
    public ResponseEntity<?> downloadFile(@PathVariable String id) {
        File file = null;
		final var fileCheck = Optional.ofNullable(fileService.getById(id));
		if (fileCheck.isPresent()) {
			file = fileCheck.get();
		}
        final String fileName = "attachment";
        final byte[] fileBytes = Base64.getDecoder().decode(file.getFileContent());
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=" + fileName + "" + file.getFileExtension()).body(fileBytes);
    }
}
