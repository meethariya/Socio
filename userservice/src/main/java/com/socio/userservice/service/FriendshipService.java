/**
 * 06-Mar-2025
 */
package com.socio.userservice.service;

import java.util.List;

import com.socio.userservice.dto.RequestFriendshipDto;
import com.socio.userservice.dto.ResponseFriendshipDto;
import com.socio.userservice.dto.ResponseUserDto;
import com.socio.userservice.model.FriendshipStatus;

/**
 * Service layer for friendship model
 */
public interface FriendshipService {

	/**
	 * Send friend request
	 * 
	 * @param friendShip {@link RequestFriendshipDto}
	 * @return {@link ResponseFriendshipDto}
	 */
	ResponseFriendshipDto createFriendShip(RequestFriendshipDto friendShip);

	/**
	 * Get friendship details
	 * 
	 * @param id friendShipId
	 * @return {@link ResponseFriendshipDto}
	 */
	ResponseFriendshipDto getFriendShipById(long id);

	/**
	 * Get all requests sent by user
	 * 
	 * @param senderId userId
	 * @return list of {@link ResponseFriendshipDto}
	 */
	List<ResponseFriendshipDto> getAllFriendShipBySender(long senderId);

	/**
	 * Get all requests received by user
	 * 
	 * @param senderId userId
	 * @return list of {@link ResponseFriendshipDto}
	 */
	List<ResponseFriendshipDto> getAllFriendShipByReceiver(long receiverId);

	/**
	 * Get all friends of user
	 * 
	 * @param senderId userId
	 * @return list of {@link ResponseFriendshipDto}
	 */
	List<ResponseUserDto> getAllFriendsOfUser(long userId);

	/**
	 * Update status of friendShip
	 * 
	 * @param id     friendShipId
	 * @param status {@link FriendshipStatus}
	 * @return {@link ResponseFriendshipDto}
	 */
	ResponseFriendshipDto updateFriendShipStatus(long id, FriendshipStatus status);

	/**
	 * Delete a friendShip
	 * 
	 * @param senderId   user id who sent request
	 * @param receiverId user id who got request
	 */
	void deleteFriendShip(long senderId, long receiverId);

	/**
	 * Check if 2 users are friends or not
	 * 
	 * @param username1 user 1
	 * @param username2 user 2
	 * @return {@link Boolean}
	 */
	boolean areFriends(String username1, String username2);
}
