/**
 * 06-Mar-2025
 */
package com.socio.userservice.dto;

import java.util.Date;

import com.socio.userservice.model.FriendshipStatus;

import lombok.Data;

/**
 * Response DTO for friendship model
 */
@Data
public class ResponseFriendshipDto {

	private long id;
	
	private ResponseUserDto sender;
	
	private ResponseUserDto receiver;
	
	private FriendshipStatus status;
	
	private Date timestamp;
}
