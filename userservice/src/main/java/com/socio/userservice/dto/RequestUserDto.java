/**
 * 03-Mar-2025
 */
package com.socio.userservice.dto;

import lombok.Data;

/**
 * Request DTO for user model 
 */
@Data
public class RequestUserDto {

	private String username;
	
	private String password;
	
	private String email;
	
	private String name;
	
}
