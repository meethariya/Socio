/**
 * 09-May-2025
 */
package com.socio.userservice.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Data;

/**
 * Save profile picture of user
 */
@Data
@Builder
public class SaveProfileDto {
	private String username;
	private MultipartFile profilePic;
}
