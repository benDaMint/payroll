package com.lawencon.payroll.service;

import java.util.ArrayList;

import com.lawencon.payroll.dto.chat.ChatMessageDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;

public interface ChatService {
    ArrayList<ChatMessageDto> getChats(String id);

    InsertResDto saveChat(ChatMessageDto chatReq);

    ChatMessageDto findChat(String id);
}
