/**
 * 09-Mar-2025
 */
package com.socio.userservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Request DTO for Auth model
 */
@Data
@Builder
public class AuthRequest {

	private String username;
	
	private String password;
}
