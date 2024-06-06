package com.lawencon.payroll.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lawencon.payroll.dto.generalResponse.UpdateResDto;
import com.lawencon.payroll.exception.FailStatementException;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(FailStatementException.class)
    public ResponseEntity<UpdateResDto> handleException(FailStatementException fse) { 
        final var updateRes = new UpdateResDto();
        updateRes.setMessage(fse.getMessage());
        return new ResponseEntity<>(updateRes, fse.getStatus());
    }
}
