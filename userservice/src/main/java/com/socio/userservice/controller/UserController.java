/**
 * 03-Mar-2025
 */
package com.socio.userservice.controller;

import java.util.List;

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

import com.socio.userservice.dto.ProfileDto;
import com.socio.userservice.dto.RequestUserDto;
import com.socio.userservice.dto.ResponseUserDto;
import com.socio.userservice.service.UserService;

import lombok.AllArgsConstructor;

/**
 * Controller layer for user model
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

	private UserService service;

	/**
	 * Create a user
	 * 
	 * @param user {@link RequestUserDto}
	 * @return {@link ResponseUserDto}
	 */
	@PostMapping
	public ResponseEntity<ResponseUserDto> createUser(@ModelAttribute RequestUserDto user) {
		return new ResponseEntity<>(service.createUser(user), HttpStatus.CREATED);
	}

	/**
	 * Get User by username
	 * 
	 * @param username {@link String}
	 * @return {@link ResponseUserDto}
	 */
	@GetMapping("/by-username/{username}")
	public ResponseEntity<ResponseUserDto> getUser(@PathVariable("username") String username) {
		return new ResponseEntity<>(service.getUser(username), HttpStatus.OK);
	}

	/**
	 * Get all users
	 * 
	 * @return List of {@link ResponseUserDto}
	 */
	@GetMapping
	public ResponseEntity<List<ResponseUserDto>> getUser() {
		return new ResponseEntity<>(service.getUser(), HttpStatus.OK);
	}

	/**
	 * Get User by authId
	 * 
	 * @param authId {@link long}
	 * @return {@link ResponseUserDto}
	 */
	@GetMapping("/auth-id/{id}")
	public ResponseEntity<ResponseUserDto> getUser(@PathVariable("id") long authId) {
		return new ResponseEntity<>(service.getUserByAuthId(authId), HttpStatus.OK);
	}

	/**
	 * Update user details
	 * 
	 * @param user {@link RequestUserDto}
	 * @param id   {@link Long}
	 * @return {@link ResponseUserDto}
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ResponseUserDto> updateUser(@ModelAttribute RequestUserDto user,
			@PathVariable("id") long id) {
		return new ResponseEntity<>(service.updateUser(user, id), HttpStatus.OK);
	}

	/**
	 * Delete user
	 * 
	 * @param id {@link Long}
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable("id") long id) {
		service.deleteUser(id);
	}

	/**
	 * Search users by username, name and email based on given query
	 * 
	 * @param query search query
	 * @return list of {@link ResponseUserDto}
	 */
	@GetMapping("/query-user")
	public ResponseEntity<List<ResponseUserDto>> queryUsers(@RequestParam("query") String query) {
		return new ResponseEntity<>(service.queryUsers(query), HttpStatus.OK);
	}

	/**
	 * Get a user's profile summary
	 * 
	 * @param username {@link String}
	 * @return {@link ProfileDto} summary
	 */
	@GetMapping("/profile-summary/{username}")
	public ResponseEntity<ProfileDto> profileSummary(@PathVariable("username") String username) {
		return new ResponseEntity<>(service.profileSummary(username), HttpStatus.OK);
	}
}
