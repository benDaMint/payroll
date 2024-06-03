package com.lawencon.payroll.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.lawencon.payroll.dto.chat.ChatMessageDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LiveChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/livechat/{roomId}")
    public void send(@DestinationVariable String roomId, ChatMessageDto message) {
        final InsertResDto insertRes = chatService.saveChat(message);
        final ChatMessageDto chatRes = chatService.findChat(insertRes.getId());

        if (!chatRes.getSenderId().equals(roomId)) {
            simpMessagingTemplate.convertAndSend("/send/livechat/" + roomId, chatRes);
        }

        String recipientRoomId = chatRes.getRecipientId();
        if (!recipientRoomId.equals(roomId)) {
            simpMessagingTemplate.convertAndSend("/send/livechat/" + recipientRoomId, chatRes);
        }
    }
}
