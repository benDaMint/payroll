package com.lawencon.payroll.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lawencon.payroll.dto.generalResponse.UpdateResDto;
import com.lawencon.payroll.exception.FailCheckException;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(FailCheckException.class)
    public ResponseEntity<UpdateResDto> handleException(FailCheckException fse) { 
        final var updateRes = new UpdateResDto();
        updateRes.setMessage(fse.getMessage());
        return new ResponseEntity<>(updateRes, fse.getStatus());
    }
}
