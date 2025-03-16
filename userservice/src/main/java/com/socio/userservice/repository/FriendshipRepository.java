/**
 * 06-Mar-2025
 */
package com.socio.userservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socio.userservice.model.Friendship;
import com.socio.userservice.model.FriendshipStatus;

/**
 * Repository layer for {@link Friendship}
 */
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

	List<Friendship> findBySenderIdAndStatus(long senderId, FriendshipStatus status);

	List<Friendship> findByReceiverIdAndStatus(long receiverId, FriendshipStatus status);
	
	List<Friendship> findBySenderIdAndStatusOrReceiverIdAndStatus(long senderId, FriendshipStatus status1, long receiverId, FriendshipStatus status2);
	
	Optional<Friendship> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(long senderId1, long receiverId1, long senderId2, long receiverId2);
}
