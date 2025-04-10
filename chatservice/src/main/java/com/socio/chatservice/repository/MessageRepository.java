/**
 * 07-Apr-2025
 */
package com.socio.chatservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.socio.chatservice.model.Message;

/**
 * Repository Layer for {@link Message}
 */
public interface MessageRepository extends MongoRepository<Message, String> {

}
