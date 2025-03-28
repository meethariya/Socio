/**
 * 26-Mar-2025
 */
package com.socio.postsservice.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.socio.postsservice.model.Like;
import com.socio.postsservice.projections.LikePostIdProjection;

/**
 * Repository layer for Post Likes
 */
public interface LikeRepository extends MongoRepository<Like, String> {

	void deleteByPostIdAndUserId(String postId, long userId);
	
	void deleteByPostId(String postId);
	
	long countByPostId(String postId);
	
	Set<LikePostIdProjection> findByPostIdInAndUserId(List<String> postIds, long userId);
	
}
