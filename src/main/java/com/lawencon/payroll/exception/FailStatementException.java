package com.lawencon.payroll.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class FailStatementException extends RuntimeException {
    private String message;
    private HttpStatus status;

    public FailStatementException() {}

    public FailStatementException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
