/**
 * 09-Mar-2025
 */
package com.socio.authservice.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.socio.authservice.dto.AuthRequest;
import com.socio.authservice.dto.AuthResponse;
import com.socio.authservice.service.AuthService;

import lombok.AllArgsConstructor;

/**
 * Controller layer for Auth
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

	private AuthService service;

	/**
	 * Create auth details
	 * 
	 * @param request {@link AuthRequest}
	 * @return {@link AuthResponse}
	 */
	@PostMapping
	public ResponseEntity<AuthResponse> createAuth(@ModelAttribute AuthRequest request) {
		return new ResponseEntity<>(service.createAuth(request), HttpStatus.CREATED);
	}

	/**
	 * Validate and generate JWT
	 * 
	 * @param request {@link AuthRequest}
	 * @return token
	 */
	@PostMapping(path = "/generate-token", produces = "text/plain")
	public String generateToken(@ModelAttribute AuthRequest request) {
		return service.generateToken(request);
	}

	/**
	 * Get auth details by username
	 * 
	 * @param request {@link AuthRequest}
	 * @return token
	 */
	@GetMapping("/get-auth/{username}")
	public ResponseEntity<AuthResponse> getAuth(@PathVariable("username") String username) {
		return new ResponseEntity<>(service.getAuth(username), HttpStatus.OK);
	}

	/**
	 * Change auth password
	 * 
	 * @param id      auth id
	 * @param request {@link AuthRequest}
	 * @return {@link AuthResponse}
	 */
	@PutMapping("/{id}")
	public ResponseEntity<AuthResponse> changePassword(@PathVariable("id") long id,
			@ModelAttribute AuthRequest request) {
		return new ResponseEntity<>(service.changePassword(id, request), HttpStatus.OK);
	}

	/**
	 * Validates the given JWT token, if invalid throws error
	 * 
	 * @param token
	 */
	@GetMapping("/validate-token")
	public ResponseEntity<Map<String, Long>> validateToken(@RequestParam("token") String token) {
		return new ResponseEntity<>(Collections.singletonMap("authId", service.validateToken(token)), HttpStatus.OK);
	}

	/**
	 * Delete auth details
	 * 
	 * @param username user's username
	 */
	@DeleteMapping("/{username}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteAuth(@PathVariable("username") String username) {
		service.deleteAuth(username);
	}
}
