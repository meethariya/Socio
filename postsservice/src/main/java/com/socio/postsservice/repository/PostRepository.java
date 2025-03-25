/**
 * 04-Mar-2025
 */
package com.socio.postsservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.socio.postsservice.model.Post;

/**
 * Repository for {@link Post}
 */
public interface PostRepository extends MongoRepository<Post, String> {

	List<Post> findByUserIdOrderByTimestampDesc(long userId);
}
