/**
 * 09-Mar-2025
 */
package com.socio.authservice.dto;

import lombok.Data;

/**
 * Request DTO for Auth model
 */
@Data
public class AuthRequest {

	private String username;
	
	private String password;
	
	private String newPassword;
}
