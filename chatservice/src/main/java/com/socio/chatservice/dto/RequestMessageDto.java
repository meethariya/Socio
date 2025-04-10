/**
 * 07-Apr-2025
 */
package com.socio.chatservice.dto;

import com.socio.chatservice.model.MessageStatus;

import lombok.Data;

/**
 * Request Dto for message
 */
@Data
public class RequestMessageDto {

	private long senderId;

	private long receiverId;

	private String content;

	private MessageStatus status;

}
