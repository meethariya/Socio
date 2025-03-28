/**
 * 26-Mar-2025
 */
package com.socio.postsservice.service;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socio.postsservice.dto.RequestLikeDto;
import com.socio.postsservice.model.Like;
import com.socio.postsservice.repository.LikeRepository;

import lombok.AllArgsConstructor;

/**
 * Implementation for {@link LikeService}
 */
@Service
@Transactional
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {

	private LikeRepository repo;
	private ModelMapper mapper;

	@Override
	public void createLike(RequestLikeDto requestLike) {
		repo.save(requestToModel(requestLike));
	}

	@Override
	public void deleteLike(RequestLikeDto requestLike) {
		repo.deleteByPostIdAndUserId(requestLike.getPostId(), requestLike.getUserId());
	}

	private Like requestToModel(RequestLikeDto requestLike) {
		Like like = mapper.map(requestLike, Like.class);
		like.setTimestamp(new Date(System.currentTimeMillis()));
		return like;
	}

}
