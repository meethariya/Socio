/**
 * 09-Mar-2025
 */
package com.socio.authservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.socio.authservice.dto.AuthRequest;
import com.socio.authservice.dto.AuthResponse;

/**
 * Service layer for Auth model. Extends {@link UserDetailsService}
 */
public interface AuthService extends UserDetailsService {

	/**
	 * Create auth details
	 * 
	 * @param request {@link AuthRequest}
	 * @return {@link AuthResponse}
	 */
	AuthResponse createAuth(AuthRequest request);

	/**
	 * Validate and generate JWT
	 * 
	 * @param request {@link AuthRequest}
	 * @return token
	 */
	String generateToken(AuthRequest request);

	/**
	 * Change auth password
	 * 
	 * @param id      auth id
	 * @param request {@link AuthRequest}
	 * @return {@link AuthResponse}
	 */
	AuthResponse changePassword(long id, AuthRequest request);

	/**
	 * Validates the given JWT token, if invalid throws error
	 * 
	 * @param token
	 * @return authId as string 
	 */
	long validateToken(String token);

	/**
	 * Delete auth details
	 * 
	 * @param username user's username
	 */
	void deleteAuth(String username);

	/**
	 * Get auth details by username
	 * 
	 * @param username user's username
	 * @return {@link AuthResponse}
	 */
	AuthResponse getAuth(String username);
}
