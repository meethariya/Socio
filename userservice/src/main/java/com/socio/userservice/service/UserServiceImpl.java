/**
 * 03-Mar-2025
 */
package com.socio.userservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.socio.userservice.dto.RequestUserDto;
import com.socio.userservice.dto.ResponseUserDto;
import com.socio.userservice.exception.UserNotFoundException;
import com.socio.userservice.model.User;
import com.socio.userservice.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/**
 * Implementation for {@link UserService}
 */
@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private ModelMapper modelMapper;
	private UserRepository repo;

	@Override
	public ResponseUserDto createUser(RequestUserDto user) {
		Optional<User> byUsername = repo.findByUsername(user.getUsername());
		if (byUsername.isPresent()) {
			throw new DataIntegrityViolationException("Username: "+user.getUsername()+" is taken");
		}

		Optional<User> byEmail = repo.findByEmail(user.getEmail());
		if (byEmail.isPresent()) {
			throw new DataIntegrityViolationException("User already exists with email: "+user.getEmail());
		}

		return modelToResponse(repo.save(requestToModel(user)));
	}

	@Override
	public ResponseUserDto getUser(long id) {
		return modelToResponse(repo.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
	}

	@Override
	public List<ResponseUserDto> getUser() {
		return repo.findAll().stream().map(this::modelToResponse).toList();
	}

	@Override
	public ResponseUserDto updateUser(RequestUserDto user, long id) {
		User user2 = repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		user2.setName(user.getName());
		return modelToResponse(repo.save(user2));
	}

	@Override
	public void deleteUser(long id) {
		repo.deleteById(id);
	}

	/**
	 * Return model from request DTO
	 * 
	 * @param user requestDTO
	 * @return user model
	 */
	private User requestToModel(RequestUserDto user) {
		return modelMapper.map(user, User.class);
	}

	/**
	 * Return response DTO from model
	 * 
	 * @param user model
	 * @return response DTO
	 */
	private ResponseUserDto modelToResponse(User user) {
		return modelMapper.map(user, ResponseUserDto.class);
	}
}
