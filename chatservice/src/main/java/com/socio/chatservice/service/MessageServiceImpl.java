/**
 * 07-Apr-2025
 */
package com.socio.chatservice.service;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socio.chatservice.dto.RequestMessageDto;
import com.socio.chatservice.exception.MessageNotFoundException;
import com.socio.chatservice.model.Message;
import com.socio.chatservice.model.MessageStatus;
import com.socio.chatservice.repository.MessageRepository;

import lombok.AllArgsConstructor;

/**
 * Implementation for {@link MessageService}
 */
@Service
@Transactional
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

	private final MessageRepository repo;
	private final ModelMapper mapper;
	private final KafkaTemplate<String, Message> kafkaTemplate;
	private final SimpMessagingTemplate messagingTemplate;
	public static final String TOPIC = "chat-messages";
	public static final String SOCKET_PATH = "/topic/messages.";

	@Override
	public void createMessage(RequestMessageDto messageDto) {
		Message message = repo.save(requestToModel(messageDto));

		String key = Math.min(message.getReceiverId(), message.getSenderId()) + "-"
				+ Math.max(message.getReceiverId(), message.getSenderId());
		kafkaTemplate.send(TOPIC, key, message);
	}

	@Override
	@KafkaListener(topics = "chat-messages", groupId = "message-consumer")
	public void consumeMessage(Message message) {
		message.setStatus(MessageStatus.SENT);
		repo.save(message);

		messagingTemplate.convertAndSend(SOCKET_PATH + message.getReceiverId(), message);
		messagingTemplate.convertAndSend(SOCKET_PATH + message.getSenderId(), message);
	}

	@Override
	public Message updateStatus(MessageStatus status, String id) {
		Message message = repo.findById(id)
				.orElseThrow(() -> new MessageNotFoundException("No message found with id: " + id));
		message.setStatus(status);
		message = repo.save(message);
		messagingTemplate.convertAndSend(SOCKET_PATH + message.getSenderId(), message);
		return message;
	}

	@Override
	public List<Message> getChatHistory(long userId, long friendId) {
		return repo.findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestamp(userId, friendId, friendId,
				userId);
	}

	@Override
	public List<Long> getUnreadMessage(long userId) {
		return repo.findByReceiverIdAndStatusOrderByTimestamp(userId, MessageStatus.SENT.toString()).stream().map(p -> p.getSenderId())
				.toList();
	}

	/**
	 * Converts request dto for message to modal
	 * 
	 * @param messageDto request dto
	 * @return {@link Message} message
	 */
	private Message requestToModel(RequestMessageDto messageDto) {
		Message message = mapper.map(messageDto, Message.class);
		message.setTimestamp(new Date(System.currentTimeMillis()));
		return message;
	}

}
