/**
 * 07-Apr-2025
 */
package com.socio.chatservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@GetMapping("/message")
	public ResponseEntity<List<Message>>getChatHistory(@RequestParam("userId") long userId, @RequestParam("friendId") long friendId){
		return new ResponseEntity<>(service.getChatHistory(userId, friendId), HttpStatus.OK);
	}
	
	@GetMapping("/message/unread-message")
	public ResponseEntity<List<Long>> getUnreadMessages(@RequestParam("userId") long userId) {
		return new ResponseEntity<>(service.getUnreadMessage(userId), HttpStatus.OK);
	}

	@PutMapping("/message/{id}")
	public ResponseEntity<Message> updateMessageStatus(@RequestBody Map<String, String> body,
			@PathVariable("id") String id) {
		if (body.get("status") == null)
			throw new MessageNotFoundException("Invalid user input");
		return new ResponseEntity<>(service.updateStatus(MessageStatus.valueOf(body.get("status")), id), HttpStatus.OK);
	}
	
	@PutMapping("/message/read-chat")
	public ResponseEntity<List<Message>> readAllMessagesOfChat(@RequestBody Map<String, Long> body) {
		if(body.get("userId") == null || body.get("friendId") == null)
			throw new MessageNotFoundException("Invalid userid and friendid");
		
		return new ResponseEntity<>(service.readAllMessages(body.get("userId"), body.get("friendId")),HttpStatus.ACCEPTED);
	}
}
