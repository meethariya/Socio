/**
 * 07-Apr-2025
 */
package com.socio.chatservice.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.socio.chatservice.dto.RequestMessageDto;
import com.socio.chatservice.exception.MessageNotFoundException;
import com.socio.chatservice.model.Message;
import com.socio.chatservice.model.MessageStatus;
import com.socio.chatservice.service.MessageService;

import lombok.AllArgsConstructor;

/**
 * Controller layer for {@link Message}
 */
@Controller
@AllArgsConstructor
public class MessageController {

	private MessageService service;

	@MessageMapping("/chat.send")
	public void createMessage(@Payload RequestMessageDto messageDto) {
		service.createMessage(messageDto);
	}

	@PutMapping("/message/{id}")
	public ResponseEntity<Message> updateMessageStatus(@RequestBody Map<String, String> body,
			@PathVariable("id") String id) {
		if (body.get("status") == null)
			throw new MessageNotFoundException("Invalid user input");
		return new ResponseEntity<>(service.updateStatus(MessageStatus.valueOf(body.get("status")), id), HttpStatus.OK);
	}
}
