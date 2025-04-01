/**
 * 01-Apr-2025
 */
package com.socio.postsservice.dto;

import lombok.Data;

/**
 * Request DTO for comment
 */
@Data
public class RequestCommentDto {

	private String postId;
	
	private String username;
	
	private String content;
}
