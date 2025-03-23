/**
 * 04-Mar-2025
 */
package com.socio.postsservice.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * Request DTO for Post model
 */
@Data
public class RequestPostDto {
	private String caption;
	private long userId;
	private MultipartFile image;
	private String location;
}
