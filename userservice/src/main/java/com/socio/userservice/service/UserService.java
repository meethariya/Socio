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
	 * Get User by Id
	 * @param id {@link Long}
	 * @return {@link ResponseUserDto}
	 */
	ResponseUserDto getUser(long id);

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

}
