/**
 * 10-May-2025
 */
package com.socio.userservice.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.socio.userservice.dto.AuthRequest;
import com.socio.userservice.dto.AuthResponse;
import com.socio.userservice.dto.SaveProfileDto;
import com.socio.userservice.openfeign.AuthClient;
import com.socio.userservice.openfeign.PostClient;

import lombok.AllArgsConstructor;

/**
 * Service to call Async logic on different threads.
 */
@Service
@AllArgsConstructor
public class AsyncService {
	private final AuthClient authClient;
    private final PostClient postClient;
	
	/**
	 * Feign client call to create Auth on separate thread
	 * 
	 * @param authRequest
	 * @return {@link AuthResponse}
	 */
	@Async(value = "taskExecutor")
	public CompletableFuture<AuthResponse> createAuthCall(AuthRequest authRequest) {
		return CompletableFuture.completedFuture(authClient.createAuth(authRequest));
	}
	
	/**
	 * Feign client call to save profile picture on separate thread
	 * @param profileDto
	 * @return profilepic path: {@link Map}
	 */
	@Async(value = "taskExecutor")
	public CompletableFuture<Map<String,String>> saveProfilePicCall(SaveProfileDto profileDto) {
		return CompletableFuture.completedFuture(postClient.saveProfile(profileDto.getUsername(), profileDto.getProfilePic()).getBody());
	}

}
