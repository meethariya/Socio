/**
 * 04-Mar-2025
 */
package com.socio.postsservice.dto;

import lombok.Data;

/**
 * Request DTO for Post model
 */
@Data
public class RequestPostDto {
	private String caption;
	private long userId;
	private String imageUrl;
	private String location;
}
