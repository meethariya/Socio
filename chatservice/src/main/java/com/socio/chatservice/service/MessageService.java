/**
 * 07-Apr-2025
 */
package com.socio.chatservice.service;

import java.util.List;

import com.socio.chatservice.dto.RequestMessageDto;
import com.socio.chatservice.model.Message;
import com.socio.chatservice.model.MessageStatus;

/**
 * Service layer for {@link Message}
 */
public interface MessageService {

	void createMessage(RequestMessageDto messageDto);
	
	Message updateStatus(MessageStatus status, String id); 
	
	void consumeMessage(Message message);
	
	List<Message> getChatHistory(long userId, long friendId);
}
