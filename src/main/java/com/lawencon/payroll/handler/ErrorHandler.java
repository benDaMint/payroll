package com.lawencon.payroll.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lawencon.payroll.dto.generalResponse.UpdateResDto;
import com.lawencon.payroll.exception.CustomException;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<UpdateResDto> handleValidation(CustomException ce) { 
        final var updateRes = new UpdateResDto();
        updateRes.setMessage(ce.getMessage());
        return new ResponseEntity<>(updateRes, ce.getStatus());
    }
}
