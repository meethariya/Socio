/**
 * 01-Apr-2025
 */
package com.socio.userservice.openfeign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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

	/**
	 * Save profile picture of user and get its path
	 * 
	 * @param profileDto image and username
	 * @return path of the image saved
	 */
	@PostMapping(value = "/post/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> saveProfile(@RequestPart("username") String username,
			@RequestPart("profilePic") MultipartFile profilePic);
}
