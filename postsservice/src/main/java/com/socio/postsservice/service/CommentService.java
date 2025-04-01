/**
 * 01-Apr-2025
 */
package com.socio.postsservice.service;

import java.util.List;

import com.socio.postsservice.dto.RequestCommentDto;
import com.socio.postsservice.dto.ResponseCommentDto;
import com.socio.postsservice.model.Comment;

/**
 * Service layer for {@link Comment}
 */
public interface CommentService {

	Long getCommentCountOfPost(String postId);
	
	List<ResponseCommentDto> getCommentsOnPost(String postId);
	
	ResponseCommentDto createComment(RequestCommentDto comment);
	
	ResponseCommentDto updateComment(RequestCommentDto comment, String commentId);
	
	void deleteComment(String commentId);
}
