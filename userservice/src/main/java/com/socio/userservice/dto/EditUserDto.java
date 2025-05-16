/**
 * 15-May-2025
 */
package com.socio.userservice.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * DTO to update user profile
 */
@Data
public class EditUserDto {
	
	private String currentPassword;
	
	private String newPassword;
	
	private String email;
	
	private String name;
	
	private MultipartFile profilePic;
}
