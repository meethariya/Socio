/**
 * 04-Mar-2025
 */
package com.socio.postsservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.socio.postsservice.dto.RequestPostDto;
import com.socio.postsservice.dto.ResponsePostDto;
import com.socio.postsservice.dto.UpdatePostDto;
import com.socio.postsservice.service.PostService;

/**
 * Controller layer for Posts
 */
@RestController
@RequestMapping("/post")
public class PostController {
	
	private PostService service;
	
	PostController(PostService service){
		this.service = service;
	}
	
	/**
	 * Create a post
	 * @param user {@link RequestPostDto}
	 * @return {@link ResponsePostDto}
	 * @throws IOException in case saving image fails
	 */
	@PostMapping
	public ResponseEntity<ResponsePostDto> createPost(@ModelAttribute RequestPostDto post) throws IOException{
		return new ResponseEntity<>(service.createPost(post), HttpStatus.CREATED);
	}
	
	/**
	 * Get Post by Id
	 * @param id {@link String}
	 * @return {@link ResponsePostDto}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ResponsePostDto> getPost(@PathVariable("id") String id){
		return new ResponseEntity<>(service.getPost(id), HttpStatus.OK);
	}
	
	/**
	 * Get all posts of user
	 * @param userId {@link Long}
	 * @return List of {@link ResponsePostDto}
	 */
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<ResponsePostDto>> getPost(@PathVariable("userId") long id){
		return new ResponseEntity<>(service.getPost(id), HttpStatus.OK);
	}
	
	/**
	 * Update post details
	 * @param user {@link RequestPostDto}
	 * @param id {@link String}
	 * @return {@link ResponsePostDto}
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ResponsePostDto> updatePost(@ModelAttribute UpdatePostDto post,@PathVariable("id") String id){
		return new ResponseEntity<>(service.updatePost(post, id), HttpStatus.OK);
	}
	
	/**
	 * Delete post
	 * @param id {@link String}
	 * @throws IOException 
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deletePost(@PathVariable("id") String id) throws IOException{
		service.deletePost(id);
	}
}
