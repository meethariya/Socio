/**
 * 03-Mar-2025
 */
package com.socio.userservice.dto;

import org.springframework.web.multipart.MultipartFile;

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
	
	private MultipartFile profilePic;
	
}
