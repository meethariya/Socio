/**
 * 04-Mar-2025
 */
package com.socio.postsservice.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socio.postsservice.dto.RequestPostDto;
import com.socio.postsservice.dto.ResponsePostDto;
import com.socio.postsservice.exception.PostNotFoundException;
import com.socio.postsservice.model.Post;
import com.socio.postsservice.repository.PostRepository;

/**
 * Implementation for Service layer of {@link Post}
 */
@Transactional
@Service
public class PostServiceImpl implements PostService {
	private ModelMapper modelMapper;
	private PostRepository repo;

	PostServiceImpl(ModelMapper modelMapper, PostRepository repo) {
		this.modelMapper = modelMapper;
		this.repo = repo;
	}

	@Override
	public ResponsePostDto createPost(RequestPostDto post) {
		return modelToResponse(repo.save(requestToModel(post)));
	}

	@Override
	public ResponsePostDto getPost(String id) {
		return modelToResponse(repo.findById(id).orElseThrow(() -> new PostNotFoundException(id)));
	}

	@Override
	public List<ResponsePostDto> getPost(long id) {
		return repo.findByUserId(id).stream().map(this::modelToResponse).toList();
	}

	@Override
	public ResponsePostDto updatePost(RequestPostDto post, String id) {
		Post postById = repo.findById(id).orElseThrow(() -> new PostNotFoundException(id));
		Post requestToModel = requestToModel(post);
		requestToModel.setId(postById.getId());
		return modelToResponse(repo.save(requestToModel));
	}

	@Override
	public void deletePost(String id) {
		repo.deleteById(id);
	}

	private Post requestToModel(RequestPostDto postDto) {
		return modelMapper.map(postDto, Post.class);
	}

	private ResponsePostDto modelToResponse(Post post) {
		return modelMapper.map(post, ResponsePostDto.class);
	}

}
