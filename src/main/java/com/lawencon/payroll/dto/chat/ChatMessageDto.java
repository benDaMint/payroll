package com.lawencon.payroll.dto.chat;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    private String message;
    private String recipientId;
    private String senderId;
    private LocalDateTime timestamp;
}
