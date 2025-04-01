/**
 * 01-Apr-2025
 */
package com.socio.userservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Response for Profile Summary
 */
@Data
@Builder
public class ProfileDto {
	private long id;
	
	private String username;
	
	private String name;
	
	private Long friendCount;
	
	private Long postCount;
}
