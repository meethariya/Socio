/**
 * 09-May-2025
 */
package com.socio.postsservice.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * Save profile picture of user
 */
@Data
public class SaveProfileDto {
	private String username;
	private MultipartFile profilePic;
}
