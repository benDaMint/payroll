package com.lawencon.payroll.service;

import java.util.Map;

import javax.mail.MessagingException;

public interface EmailService {
    void sendEmail(String to, String subject, Map<String, Object> templateBody) throws MessagingException;
}
