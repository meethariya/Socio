/**
 * 26-Mar-2025
 */
package com.socio.postsservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.socio.postsservice.dto.RequestLikeDto;
import com.socio.postsservice.service.LikeService;

import lombok.AllArgsConstructor;

/**
 * Controller Layer for likes of a post
 */
@RestController
@RequestMapping("/like")
@AllArgsConstructor
public class LikeController {

	private LikeService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void addLikeOnPost(@ModelAttribute RequestLikeDto likeDto) {
		service.createLike(likeDto);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void removeLikeOnPost(@ModelAttribute RequestLikeDto likeDto) {
		service.deleteLike(likeDto);
	}
}
