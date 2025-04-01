/**
 * 01-Apr-2025
 */
package com.socio.postsservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.socio.postsservice.model.Comment;

/**
 * Repository layer for comments
 */
public interface CommentRepository extends MongoRepository<Comment, String>{

	long countByPostId(String postId);
	
	List<Comment> findByPostId(String postId);
	
	void deleteByPostId(String postId);
}
