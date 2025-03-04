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

	ResponseUserDto createUser(RequestUserDto user);

	ResponseUserDto getUser(long id);

	List<ResponseUserDto> getUser();

	ResponseUserDto updateUser(RequestUserDto user, long id);

	void deleteUser(long id);

}
