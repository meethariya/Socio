/**
 * 01-Apr-2025
 */
package com.socio.postsservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socio.postsservice.dto.RequestCommentDto;
import com.socio.postsservice.dto.ResponseCommentDto;
import com.socio.postsservice.service.CommentService;

import lombok.AllArgsConstructor;

/**
 * Controller layer for comments
 */
@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

	private CommentService service;

	@PostMapping
	public ResponseEntity<ResponseCommentDto> createComment(@ModelAttribute RequestCommentDto commentDto) {
		return new ResponseEntity<>(service.createComment(commentDto), HttpStatus.CREATED);
	}

	@GetMapping("/count/{postId}")
	public ResponseEntity<Map<String, Long>> getCommentCountOnPost(@PathVariable("postId") String postId) {
		Map<String, Long> response = new HashMap<>();
		response.put("count", service.getCommentCountOfPost(postId));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/by-post/{postId}")
	public ResponseEntity<List<ResponseCommentDto>> getCommentsOnPost(@PathVariable("postId") String postId) {
		return new ResponseEntity<>(service.getCommentsOnPost(postId), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseCommentDto> updateComment(@PathVariable("id") String id,
			@ModelAttribute RequestCommentDto commentDto) {
		return new ResponseEntity<>(service.updateComment(commentDto, id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public void deleteComment(@PathVariable("id") String id) {
		service.deleteComment(id);
	}
}
