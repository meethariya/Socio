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

	/**
	 * Requests sent by user
	 * 
	 * @param senderId user id
	 * @param status   pending
	 * @return {@link List}
	 */
	List<Friendship> findBySenderIdAndStatus(long senderId, FriendshipStatus status);

	/**
	 * Requests received by user
	 * 
	 * @param receiverId user id
	 * @param status     pending
	 * @return {@link List}
	 */
	List<Friendship> findByReceiverIdAndStatus(long receiverId, FriendshipStatus status);

	/**
	 * All friends of user. here sender and receiver is same
	 * 
	 * @param senderId   user id
	 * @param status1    accepted
	 * @param receiverId user id
	 * @param status2    accepted
	 * @return {@link List}
	 */
	List<Friendship> findBySenderIdAndStatusOrReceiverIdAndStatus(long senderId, FriendshipStatus status1,
			long receiverId, FriendshipStatus status2);

	/**
	 * Check if 2 users are friends
	 * 
	 * @param senderId1   user1 id
	 * @param receiverId1 user2 id
	 * @param senderId2   user2 id
	 * @param receiverId2 user1 id
	 * @return {@link Friendship}
	 */
	Optional<Friendship> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(long senderId1, long receiverId1,
			long senderId2, long receiverId2);

	/**
	 * Check if 2 users are friends
	 * 
	 * @param senderId1   user1 username
	 * @param receiverId1 user2 username
	 * @param senderId2   user2 username
	 * @param receiverId2 user1 username
	 * @return {@link Friendship}
	 */
	Optional<Friendship> findBySenderUsernameAndReceiverUsernameOrSenderUsernameAndReceiverUsername(
			String senderUsername1, String receiverUsername1, String senderUsername2, String receiverUsername2);

	/**
	 * Delete friendship between 2 users
	 * 
	 * @param senderId1   user1 id
	 * @param receiverId1 user2 id
	 * @param senderId2   user2 id
	 * @param receiverId2 user1 id
	 */
	void deleteBySenderIdAndReceiverIdOrSenderIdAndReceiverId(long senderId1, long receiverId1, long senderId2,
			long receiverId2);
}
