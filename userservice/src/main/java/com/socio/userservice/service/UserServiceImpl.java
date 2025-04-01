/**
 * 03-Mar-2025
 */
package com.socio.userservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.socio.userservice.dto.AuthRequest;
import com.socio.userservice.dto.AuthResponse;
import com.socio.userservice.dto.ProfileDto;
import com.socio.userservice.dto.RequestUserDto;
import com.socio.userservice.dto.ResponseUserDto;
import com.socio.userservice.exception.UserNotFoundException;
import com.socio.userservice.model.FriendshipStatus;
import com.socio.userservice.model.User;
import com.socio.userservice.openfeign.AuthClient;
import com.socio.userservice.openfeign.PostClient;
import com.socio.userservice.repository.FriendshipRepository;
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
	private FriendshipRepository friendRepo;
	private AuthClient authClient;
	private PostClient postClient;

	@Override
	public ResponseUserDto createUser(RequestUserDto user) {
		Optional<User> byUsername = repo.findByUsername(user.getUsername());
		if (byUsername.isPresent()) {
			throw new DataIntegrityViolationException("Username: " + user.getUsername() + " is taken");
		}

		Optional<User> byEmail = repo.findByEmail(user.getEmail());
		if (byEmail.isPresent()) {
			throw new DataIntegrityViolationException("User already exists with email: " + user.getEmail());
		}
		AuthRequest authRequest = AuthRequest.builder().username(user.getUsername()).password(user.getPassword())
				.build();
		AuthResponse auth = authClient.createAuth(authRequest);
		User requestToModel = requestToModel(user);
		requestToModel.setAuthId(auth.getId());
		return modelToResponse(repo.save(requestToModel));
	}

	@Override
	public ResponseUserDto getUser(String username) {
		return modelToResponse(repo.findByUsername(username).orElseThrow(() -> new UserNotFoundException("No user found with username: "+username)));
	}

	@Override
	public List<ResponseUserDto> getUser() {
		return repo.findAll().stream().map(this::modelToResponse).toList();
	}
	
	@Override
	public ResponseUserDto getUserByAuthId(long authId) {
		return modelToResponse(repo.findByAuthId(authId).orElseThrow(() -> new UserNotFoundException("No user found with authId: "+authId)));
	}

	@Override
	public ResponseUserDto updateUser(RequestUserDto user, long id) {
		User user2 = repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		user2.setName(user.getName());
		return modelToResponse(repo.save(user2));
	}

	@Override
	public void deleteUser(long id) {
		User user2 = repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		repo.deleteById(id);
		authClient.deleteAuth(user2.getUsername());
	}
	
	@Override
	public List<ResponseUserDto> queryUsers(String query) {
		return repo.findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query, query).stream().map(this::modelToResponse).toList();
	}
	
	@Override
	public ProfileDto profileSummary(String username) {
		User user = repo.findByUsername(username).orElseThrow(()-> new UserNotFoundException("No user found with username: "+username));
		Long postCount = postClient.getPostCount(user.getId());
		Long friendCount = (long) friendRepo.findBySenderIdAndStatusOrReceiverIdAndStatus(user.getId(), FriendshipStatus.ACCEPTED, user.getId(), FriendshipStatus.ACCEPTED).size();
		return ProfileDto.builder()
				.id(user.getId())
				.username(username)
				.name(user.getName())
				.friendCount(friendCount)
				.postCount(postCount)
				.build();
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
