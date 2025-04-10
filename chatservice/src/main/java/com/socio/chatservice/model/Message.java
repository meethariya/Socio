/**
 * 07-Apr-2025
 */
package com.socio.chatservice.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

/**
 * Message model
 */
@Data
@Document
public class Message {

	@MongoId
	private String id;
	
	private long senderId;
	
	private long receiverId;
	
	private String content;
	
	private MessageStatus status;
	
	private Date timestamp;
}
