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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.socio.userservice.dto.RequestUserDto;
import com.socio.userservice.dto.ResponseUserDto;
import com.socio.userservice.service.UserService;

/**
 * Controller layer for user model
 */
@RestController
@RequestMapping("/user")
public class UserController {

	private UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<ResponseUserDto> createUser(@ModelAttribute RequestUserDto user) {
		return new ResponseEntity<>(service.createUser(user), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseUserDto> getUser(@PathVariable("id") long id) {
		return new ResponseEntity<>(service.getUser(id), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<ResponseUserDto>> getUser() {
		return new ResponseEntity<>(service.getUser(), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseUserDto> updateUser(@ModelAttribute RequestUserDto user,
			@PathVariable("id") long id) {
		return new ResponseEntity<>(service.updateUser(user, id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable("id") long id) {
		service.deleteUser(id);
	}
}
