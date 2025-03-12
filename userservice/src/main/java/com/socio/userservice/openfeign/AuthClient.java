/**
 * 11-Mar-2025
 */
package com.socio.userservice.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.socio.userservice.dto.AuthRequest;
import com.socio.userservice.dto.AuthResponse;

/**
 * Feign client for Auth service
 */
@FeignClient(name = "authservice", configuration = FeignConfig.class)
public interface AuthClient {

	/**
	 * Create auth credentials
	 * 
	 * @param request {@link AuthRequest}
	 * @return {@link AuthResponse}
	 */
	@PostMapping(path = "/auth", consumes = "multipart/form-data")
	public AuthResponse createAuth(AuthRequest request);

	/**
	 * Delete auth credentials
	 * 
	 * @param username user's username
	 */
	@DeleteMapping("/auth/{username}")
	public void deleteAuth(@PathVariable("username") String username);
}
