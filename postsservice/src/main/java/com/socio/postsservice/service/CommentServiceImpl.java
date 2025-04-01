/**
 * 01-Apr-2025
 */
package com.socio.postsservice.service;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socio.postsservice.dto.RequestCommentDto;
import com.socio.postsservice.dto.ResponseCommentDto;
import com.socio.postsservice.exception.CommentNotFoundException;
import com.socio.postsservice.model.Comment;
import com.socio.postsservice.repository.CommentRepository;

import lombok.AllArgsConstructor;

/**
 * Implementation for service layer of comments
 */
@Service
@Transactional
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

	private CommentRepository repo;
	private ModelMapper mapper;

	@Override
	public Long getCommentCountOfPost(String postId) {
		return repo.countByPostId(postId);
	}

	@Override
	public List<ResponseCommentDto> getCommentsOnPost(String postId) {
		return repo.findByPostId(postId).stream().map(this::modelToResponse).toList();
	}

	@Override
	public ResponseCommentDto createComment(RequestCommentDto comment) {
		return modelToResponse(repo.save(requestToModel(comment)));
	}

	@Override
	public ResponseCommentDto updateComment(RequestCommentDto comment, String commentId) {
		Comment comment2 = repo.findById(commentId).orElseThrow(()-> new CommentNotFoundException(commentId));
		
		if(!comment.getContent().isEmpty()) {
			comment2.setContent(comment.getContent());
			comment2.setTimestamp(new Date(System.currentTimeMillis()));
			comment2.setEdited(true);
		}
		
		return modelToResponse(repo.save(comment2));
	}

	@Override
	public void deleteComment(String commentId) {
		repo.deleteById(commentId);
	}

	private Comment requestToModel(RequestCommentDto requestComment) {
		Comment comment = mapper.map(requestComment, Comment.class);
		comment.setTimestamp(new Date(System.currentTimeMillis()));
		comment.setEdited(false);
		return comment;
	}
	
	private ResponseCommentDto modelToResponse(Comment comment) {
		return mapper.map(comment, ResponseCommentDto.class);
	}
}
