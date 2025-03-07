/**
 * 06-Mar-2025
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.socio.userservice.dto.RequestFriendshipDto;
import com.socio.userservice.dto.ResponseFriendshipDto;
import com.socio.userservice.model.FriendshipStatus;
import com.socio.userservice.service.FriendshipService;

import lombok.AllArgsConstructor;

/**
 * Controller layer for friendship model
 */
@RestController
@RequestMapping("/friend")
@AllArgsConstructor
public class FriendshipController {

	private FriendshipService service;
	
	/**
	 * Create new  friend request
	 * 
	 * @param friendShip {@link RequestFriendshipDto}
	 * @return {@link ResponseFriendshipDto}
	 */
	@PostMapping
	public ResponseEntity<ResponseFriendshipDto> createFriendShip(@ModelAttribute RequestFriendshipDto friendShip) {
		return new ResponseEntity<>(service.createFriendShip(friendShip), HttpStatus.CREATED);
	}

	/**
	 * Get friendship details
	 * 
	 * @param id friendShipId
	 * @return {@link ResponseFriendshipDto}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ResponseFriendshipDto> getFriendShipById(@PathVariable("id") long id) {
		return new ResponseEntity<>(service.getFriendShipById(id), HttpStatus.OK);
	}

	/**
	 * Get all requests sent by user
	 * 
	 * @param senderId userId
	 * @return list of {@link ResponseFriendshipDto}
	 */
	@GetMapping("/request-sent-by-user/{id}")
	public ResponseEntity<List<ResponseFriendshipDto>> getAllFriendShipBySender(@PathVariable("id") long senderId) {
		return new ResponseEntity<>(service.getAllFriendShipBySender(senderId), HttpStatus.OK);
	}

	/**
	 * Get all requests received by user
	 * 
	 * @param senderId userId
	 * @return list of {@link ResponseFriendshipDto}
	 */
	@GetMapping("/request-received-by-user/{id}")
	public ResponseEntity<List<ResponseFriendshipDto>> getAllFriendShipByReceiver(@PathVariable("id") long receiverId) {
		return new ResponseEntity<>(service.getAllFriendShipByReceiver(receiverId), HttpStatus.OK);
	}

	/**
	 * Get all friends of user
	 * 
	 * @param senderId userId
	 * @return list of {@link ResponseFriendshipDto}
	 */
	@GetMapping("/friends-of-user/{id}")
	public ResponseEntity<List<ResponseFriendshipDto>> getAllFriendsOfUser(@PathVariable("id") long userId) {
		return new ResponseEntity<>(service.getAllFriendsOfUser(userId), HttpStatus.OK);
	}

	/**
	 * Update status of friendShip
	 * 
	 * @param id     friendShipId
	 * @param status {@link FriendshipStatus}
	 * @return {@link ResponseFriendshipDto}
	 */
	@PutMapping(path = "/{id}")
	public ResponseEntity<ResponseFriendshipDto> updateFriendShipStatus(@PathVariable("id") long id, @RequestBody String status) {
		FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(status.toUpperCase());
		return new ResponseEntity<>(service.updateFriendShipStatus(id, friendshipStatus), HttpStatus.OK);
	}

	/**
	 * Delete a friendShip
	 * 
	 * @param id friendShipId
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	void deleteFriendShip(@PathVariable("id") long id) {
		service.deleteFriendShip(id);
	}
}
