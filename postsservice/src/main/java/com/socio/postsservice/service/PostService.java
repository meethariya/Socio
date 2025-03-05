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

	ResponsePostDto createPost(RequestPostDto post);
	
	ResponsePostDto getPost(String id);
	
	List<ResponsePostDto> getPost();
	
	ResponsePostDto updatePost(RequestPostDto post, String id);
	
	void deletePost(String id);
}
