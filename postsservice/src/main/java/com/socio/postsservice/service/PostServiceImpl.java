/**
 * 04-Mar-2025
 */
package com.socio.postsservice.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.socio.postsservice.dto.RequestPostDto;
import com.socio.postsservice.dto.ResponsePostDto;
import com.socio.postsservice.dto.UpdatePostDto;
import com.socio.postsservice.exception.InvalidInputException;
import com.socio.postsservice.exception.PostNotFoundException;
import com.socio.postsservice.model.Post;
import com.socio.postsservice.projections.LikePostIdProjection;
import com.socio.postsservice.repository.LikeRepository;
import com.socio.postsservice.repository.PostRepository;

/**
 * Implementation for Service layer of {@link Post}
 */
@Transactional
@Service
public class PostServiceImpl implements PostService {
	private ModelMapper modelMapper;
	private PostRepository repo;
	private LikeRepository likeRepo;

	private static final String UPLOAD_DIR = "/uploads";

	PostServiceImpl(ModelMapper modelMapper, PostRepository repo, LikeRepository likeRepo) {
		this.modelMapper = modelMapper;
		this.repo = repo;
		this.likeRepo = likeRepo;
	}

	@Override
	public ResponsePostDto createPost(RequestPostDto post) throws IOException {
		MultipartFile image = post.getImage();

		if (image.isEmpty()) {
			throw new InvalidInputException("No image found for post");
		}

		String path = UPLOAD_DIR + File.separator + post.getUserId() + File.separator;

		File uploadDir = new File(path);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		String name = image.getOriginalFilename();
		if (name != null) {
			name = name.replace(" ", "_");
		} else {
			name = "image.png";
		}

		String fileName = System.currentTimeMillis() + "_" + name;
		Path filePath = Paths.get(path, fileName);
		Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		// Build only the path
		String imagePath = UriComponentsBuilder
				.fromPath(UPLOAD_DIR + File.separator + post.getUserId() + File.separator + fileName).build()
				.toUriString();

		Post model = requestToModel(post);
		model.setImageUrl(imagePath);

		return modelToResponse(repo.save(model));
	}

	@Override
	public ResponsePostDto getPost(String id) {
		return modelToResponse(repo.findById(id).orElseThrow(() -> new PostNotFoundException(id)));
	}

	@Override
	public List<ResponsePostDto> getPost(long id, Object visitorId) {
		List<ResponsePostDto> response = repo.findByUserIdOrderByTimestampDesc(id).stream().map(this::modelToResponse)
				.toList();
		if (visitorId == null)
			return response;
		long vId = Long.parseLong(String.valueOf(visitorId));
		Set<String> likesOfVisitor = likeRepo
				.findByPostIdInAndUserId(response.stream().map(ResponsePostDto::getId).toList(), vId)
				.stream().map(LikePostIdProjection::getPostId).collect(Collectors.toSet());
		return response.stream().map(r -> {
			r.setLikedByVisitor(likesOfVisitor.contains(r.getId()));
			return r;
		}).toList();
	}
	

	@Override
	public Long getPostCount(Long userId) {
		return repo.countByUserId(userId);
	}

	@Override
	public ResponsePostDto updatePost(UpdatePostDto post, String id) {
		Post postById = repo.findById(id).orElseThrow(() -> new PostNotFoundException(id));

		if (post.getCaption() != null) {
			postById.setCaption(post.getCaption());
		}

		if (post.getLocation() != null) {
			postById.setLocation(post.getLocation());
		}

		postById.setCovered(post.isCovered());
		postById.setNeedBlurBackground(post.isNeedBlurBackground());

		return modelToResponse(repo.save(postById));
	}

	@Override
	public void deletePost(String id) throws IOException {
		Post post = repo.findById(id).orElseThrow(() -> new PostNotFoundException(id));
		Files.deleteIfExists(Paths.get(post.getImageUrl()));
		repo.deleteById(id);
		likeRepo.deleteByPostId(id);
	}

	private Post requestToModel(RequestPostDto postDto) {
		Post post = modelMapper.map(postDto, Post.class);
		post.setTimestamp(new Date(System.currentTimeMillis()));
		return post;
	}

	private ResponsePostDto modelToResponse(Post post) {
		ResponsePostDto dto = modelMapper.map(post, ResponsePostDto.class);
		dto.setLikeCount(likeRepo.countByPostId(post.getId()));
		return dto;
	}

}
