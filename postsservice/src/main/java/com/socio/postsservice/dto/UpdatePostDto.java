/**
 * 24-Mar-2025
 */
package com.socio.postsservice.dto;

import lombok.Data;

/**
 * Request DTO to update the post
 */
@Data
public class UpdatePostDto {

	private String caption;
	private String location;
	private boolean covered;
	private boolean needBlurBackground;
}
