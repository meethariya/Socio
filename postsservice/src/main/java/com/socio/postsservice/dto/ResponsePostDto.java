/**
 * 04-Mar-2025
 */
package com.socio.postsservice.dto;

import lombok.Data;

/**
 * Response DTO for Post Model
 */
@Data
public class ResponsePostDto {
	private String id;
	private String caption;
	private long userId;
	private String imageUrl;
	private String location;
	private boolean covered;
	private boolean needBlurBackground;
}
