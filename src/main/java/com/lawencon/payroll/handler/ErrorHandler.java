package com.lawencon.payroll.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lawencon.payroll.dto.generalResponse.UpdateResDto;
import com.lawencon.payroll.exception.ComparisonNotMatchException;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ComparisonNotMatchException.class)
    public ResponseEntity<UpdateResDto> handleValidation(ComparisonNotMatchException ce) { 
        final var updateRes = new UpdateResDto();
        updateRes.setMessage(ce.getMessage());
        return new ResponseEntity<>(updateRes, ce.getStatus());
    }
}
