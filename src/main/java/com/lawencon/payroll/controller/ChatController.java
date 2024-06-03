package com.lawencon.payroll.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.payroll.dto.chat.ChatMessageDto;
import com.lawencon.payroll.dto.generalResponse.InsertResDto;
import com.lawencon.payroll.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("chat")
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@GetMapping("{id}")
	public ResponseEntity<ArrayList<ChatMessageDto>> chatContents(@PathVariable String id) {
		final ArrayList<ChatMessageDto> chatsRes = chatService.getChats(id);
		return new ResponseEntity<>(chatsRes, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<InsertResDto> saveChat(@RequestBody ChatMessageDto chat) {
		final InsertResDto insertRes = chatService.saveChat(chat);
		return new ResponseEntity<>(insertRes, HttpStatus.OK);
	}
}
