/**
 * 07-Apr-2025
 */
package com.socio.chatservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.socio.chatservice.model.Message;
import com.socio.chatservice.projections.MessageSenderIdProjection;

/**
 * Repository Layer for {@link Message}
 */
public interface MessageRepository extends MongoRepository<Message, String> {

	List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestamp(long senderId1, long receiverId1, long senderId2, long receiverId2);
	
	List<Message> findBySenderIdAndReceiverIdAndStatusOrSenderIdAndReceiverIdAndStatusOrderByTimestamp(long senderId1, long receiverId1, String status1, long senderId2, long receiverId2, String status2);
	
	List<MessageSenderIdProjection> findByReceiverIdAndStatusOrderByTimestamp(long receiverId, String status);
}
