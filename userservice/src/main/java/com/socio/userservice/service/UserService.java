/**
 * 03-Mar-2025
 */
package com.socio.userservice.service;

import java.util.List;

import com.socio.userservice.dto.RequestUserDto;
import com.socio.userservice.dto.ResponseUserDto;

/**
 * Service layer for user model
 */
public interface UserService {

	/**
	 * Create a user
	 * @param user {@link RequestUserDto}
	 * @return {@link ResponseUserDto}
	 */
	ResponseUserDto createUser(RequestUserDto user);

	/**
	 * Get User by username
	 * @param username {@link String}
	 * @return {@link ResponseUserDto}
	 */
	ResponseUserDto getUser(String username);
	
	/**
	 * Get User by auth id
	 * @param authId {@link Long}
	 * @return {@link ResponseUserDto}
	 */
	ResponseUserDto getUserByAuthId(long authId);

	/**
	 * Get all users
	 * @return List of {@link ResponseUserDto}
	 */
	List<ResponseUserDto> getUser();

	/**
	 * Update user details
	 * @param user {@link RequestUserDto}
	 * @param id {@link Long}
	 * @return {@link ResponseUserDto}
	 */
	ResponseUserDto updateUser(RequestUserDto user, long id);

	/**
	 * Delete user
	 * @param id {@link Long}
	 */
	void deleteUser(long id);
	
	/**
	 * Search all users for its username, name and email
	 * @param query search parameter
	 * @return list of relevant users
	 */
	List<ResponseUserDto> queryUsers(String query);

}
