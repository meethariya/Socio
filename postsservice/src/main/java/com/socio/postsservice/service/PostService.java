/**
 * 04-Mar-2025
 */
package com.socio.postsservice.service;

import java.io.IOException;
import java.util.List;

import com.socio.postsservice.dto.RequestPostDto;
import com.socio.postsservice.dto.ResponsePostDto;
import com.socio.postsservice.dto.SaveProfileDto;
import com.socio.postsservice.dto.UpdatePostDto;

/**
 * Service layer for Post
 */
public interface PostService {

	/**
	 * Create a post
	 * @param user {@link RequestPostDto}
	 * @return {@link ResponsePostDto}
	 * @throws IOException in case saving image fails
	 */
	ResponsePostDto createPost(RequestPostDto post) throws IOException;
	
	/**
	 * Get Post by Id
	 * @param id {@link String}
	 * @return {@link ResponsePostDto}
	 */
	ResponsePostDto getPost(String id);
	
	/**
	 * Get all posts of user
	 * @param id {@link Long}
	 * @param visitorId null or {@link Long}  
	 * @return List of {@link ResponsePostDto}
	 */
	List<ResponsePostDto> getPost(long id, Object visitorId);
	
	/**
	 * Get count of posts of user
	 * @param userId {@link Long}
	 * @return number of posts {@link Long}
	 */
	Long getPostCount(Long userId);
	
	/**
	 * Update post details
	 * @param user {@link UpdatePostDto}
	 * @param id {@link String}
	 * @return {@link ResponsePostDto}
	 */
	ResponsePostDto updatePost(UpdatePostDto post, String id);
	
	/**
	 * Delete post
	 * @param id {@link String}
	 * @throws IOException in case deletion of image fails 
	 */
	void deletePost(String id) throws IOException;
	
	/**
	 * Save profile picture of user to file system
	 * @param profileDto
	 * @return path of the picture saved to
	 * @throws IOException in case saving image fails
	 */
	String saveProfileImage(SaveProfileDto profileDto) throws IOException;
}
