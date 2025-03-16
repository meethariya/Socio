/**
 * 06-Mar-2025
 */
package com.socio.userservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socio.userservice.dto.RequestFriendshipDto;
import com.socio.userservice.dto.ResponseFriendshipDto;
import com.socio.userservice.dto.ResponseUserDto;
import com.socio.userservice.exception.DuplicateFriendshipException;
import com.socio.userservice.exception.FriendshipNotFoundException;
import com.socio.userservice.exception.UserNotFoundException;
import com.socio.userservice.model.Friendship;
import com.socio.userservice.model.FriendshipStatus;
import com.socio.userservice.model.User;
import com.socio.userservice.repository.FriendshipRepository;
import com.socio.userservice.repository.UserRepository;

import lombok.AllArgsConstructor;

/**
 * Implementation for {@link FriendshipService}
 */
@Service
@Transactional
@AllArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {

	private FriendshipRepository friendshipRepository;
	private UserRepository userRepository;
	private ModelMapper mapper;

	@Override
	public ResponseFriendshipDto createFriendShip(RequestFriendshipDto friendShip) {
		long senderId = friendShip.getSenderId();
		long receiverId = friendShip.getReceiverId();
		Optional<Friendship> optional = friendshipRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(senderId, receiverId, receiverId, senderId);
		optional.ifPresent(f->{
			throw new DuplicateFriendshipException(f.getId());
		});
		return modelToResponse(friendshipRepository.save(requestToModel(friendShip)));
	}

	@Override
	public ResponseFriendshipDto getFriendShipById(long id) {
		return modelToResponse(friendshipRepository.findById(id).orElseThrow(() -> new FriendshipNotFoundException(id)));
	}

	@Override
	public List<ResponseFriendshipDto> getAllFriendShipBySender(long senderId) {
		return friendshipRepository.findBySenderIdAndStatus(senderId, FriendshipStatus.PENDING).stream().map(this::modelToResponse).toList();
	}

	@Override
	public List<ResponseFriendshipDto> getAllFriendShipByReceiver(long receiverId) {
		return friendshipRepository.findByReceiverIdAndStatus(receiverId, FriendshipStatus.PENDING).stream().map(this::modelToResponse).toList();
	}

	@Override
	public List<ResponseUserDto> getAllFriendsOfUser(long userId) {
		return friendshipRepository
				.findBySenderIdAndStatusOrReceiverIdAndStatus(userId, FriendshipStatus.ACCEPTED, userId, FriendshipStatus.ACCEPTED)
				.stream()
				.map(friend -> modelToResponse(friend.getReceiver().getId()==userId ? friend.getSender() : friend.getReceiver()))
				.toList();
	}

	@Override
	public ResponseFriendshipDto updateFriendShipStatus(long id, FriendshipStatus status) {
		Friendship friendship = friendshipRepository.findById(id).orElseThrow(() -> new FriendshipNotFoundException(id));
		friendship.setStatus(status);
		return modelToResponse(friendshipRepository.save(friendship));
	}

	@Override
	public void deleteFriendShip(long id) {
		friendshipRepository.deleteById(id);
	}
	
	/**
	 * Converts request friendship dto to model dto. Also fetches sender and receiver user models
	 * @param dto {@link RequestFriendshipDto}
	 * @return {@link Friendship}
	 */
	private Friendship requestToModel(RequestFriendshipDto dto) {
		if(dto.getStatus() == null) dto.setStatus(FriendshipStatus.PENDING);
		Friendship friendship = mapper.map(dto, Friendship.class);
		User sender = userRepository.findById(dto.getSenderId()).orElseThrow(() -> new UserNotFoundException(dto.getSenderId()));
		User receiver = userRepository.findById(dto.getReceiverId()).orElseThrow(() -> new UserNotFoundException(dto.getReceiverId()));
		friendship.setSender(sender);
		friendship.setReceiver(receiver);
		return friendship;
	}
	/**
	 * Converts model friendship to response model dto. Also converts sender, receiver user model to {@link ResponseUserDto}
	 * @param friendship model
	 * @return {@link ResponseFriendshipDto}
	 */
	private ResponseFriendshipDto modelToResponse(Friendship friendship) {
		ResponseFriendshipDto dto = mapper.map(friendship, ResponseFriendshipDto.class);
		dto.setSender(modelToResponse(friendship.getSender()));
		dto.setReceiver(modelToResponse(friendship.getReceiver()));
		return dto;
	}
	
	/**
	 * Return response DTO from model
	 * 
	 * @param user model
	 * @return response DTO
	 */
	private ResponseUserDto modelToResponse(User user) {
		return mapper.map(user, ResponseUserDto.class);
	}
}
