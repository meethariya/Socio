/**
 * 01-Apr-2025
 */
package com.socio.userservice.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for Post service
 */
@FeignClient(name = "postsservice", configuration = FeignConfig.class)
public interface PostClient {

	/**
	 * Get count of posts of user.
	 * 
	 * @param id userId {@link Long}
	 * @return number of posts {@link Long}
	 */
	@GetMapping("/post/user/post-count/{userId}")
	public Long getPostCount(@PathVariable("userId") long id);
}
