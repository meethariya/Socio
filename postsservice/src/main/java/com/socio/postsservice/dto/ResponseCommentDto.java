/**
 * 01-Apr-2025
 */
package com.socio.postsservice.dto;

import java.util.Date;

import lombok.Data;

/**
 * Response DTO for comment
 */
@Data
public class ResponseCommentDto {
	private String id;

	private String postId;

	private String username;

	private String content;
	
	private boolean edited;

	private Date timestamp;
}
