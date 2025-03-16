/**
 * 04-Mar-2025
 */
package com.socio.postsservice.service;

import java.util.List;

import com.socio.postsservice.dto.RequestPostDto;
import com.socio.postsservice.dto.ResponsePostDto;

/**
 * Service layer for Post
 */
public interface PostService {

	/**
	 * Create a post
	 * @param user {@link RequestPostDto}
	 * @return {@link ResponsePostDto}
	 */
	ResponsePostDto createPost(RequestPostDto post);
	
	/**
	 * Get Post by Id
	 * @param id {@link String}
	 * @return {@link ResponsePostDto}
	 */
	ResponsePostDto getPost(String id);
	
	/**
	 * Get all posts of user
	 * @param id {@link Long}
	 * @return List of {@link ResponsePostDto}
	 */
	List<ResponsePostDto> getPost(long id);
	
	/**
	 * Update post details
	 * @param user {@link RequestPostDto}
	 * @param id {@link String}
	 * @return {@link ResponsePostDto}
	 */
	ResponsePostDto updatePost(RequestPostDto post, String id);
	
	/**
	 * Delete post
	 * @param id {@link String}
	 */
	void deletePost(String id);
}
