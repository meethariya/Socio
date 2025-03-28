/**
 * 26-Mar-2025
 */
package com.socio.postsservice.service;

import com.socio.postsservice.dto.RequestLikeDto;

/**
 * Service layer for post likes
 */
public interface LikeService {

	void createLike(RequestLikeDto requestLike);
	
	void deleteLike(RequestLikeDto requestLike);
}
