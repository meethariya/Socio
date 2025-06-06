/**
 * 03-Mar-2025
 */
package com.socio.userservice.dto;

import lombok.Data;

/**
 * Response DTO for User model
 */
@Data
public class ResponseUserDto {

	private long id;
	
	private String username;
	
	private String email;
	
	private String name; 
	
	private long authId;
	
	private String profilePic;
}
