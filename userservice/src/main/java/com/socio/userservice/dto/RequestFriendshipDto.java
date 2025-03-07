/**
 * 06-Mar-2025
 */
package com.socio.userservice.dto;

import com.socio.userservice.model.FriendshipStatus;

import lombok.Data;

/**
 * Request DTO for friendship model
 */
@Data
public class RequestFriendshipDto {

	private long senderId;
	
	private long receiverId;
	
	private FriendshipStatus status;
}
