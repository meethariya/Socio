/**
 * 26-Mar-2025
 */
package com.socio.postsservice.dto;

import lombok.Data;

/**
 * To create a like on post
 */
@Data
public class RequestLikeDto {

	private String postId;
	
	private long userId;
}
