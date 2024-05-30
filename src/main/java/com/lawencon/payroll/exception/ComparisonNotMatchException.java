package com.lawencon.payroll.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ComparisonNotMatchException extends RuntimeException {
    private String message;
    private HttpStatus status;

    public ComparisonNotMatchException() {}

    public ComparisonNotMatchException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
