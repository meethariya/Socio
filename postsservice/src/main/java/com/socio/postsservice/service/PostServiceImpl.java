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
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.socio.postsservice.dto.RequestPostDto;
import com.socio.postsservice.dto.ResponsePostDto;
import com.socio.postsservice.exception.InvalidInputException;
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
	
	private static final String UPLOAD_DIR = "/uploads";

	PostServiceImpl(ModelMapper modelMapper, PostRepository repo) {
		this.modelMapper = modelMapper;
		this.repo = repo;
	}

	@Override
	public ResponsePostDto createPost(RequestPostDto post) throws IOException {
		MultipartFile image = post.getImage();
		
		if(image.isEmpty()) {
			throw new InvalidInputException("No image found for post");
		}
		
		String path = UPLOAD_DIR + File.separator + post.getUserId() + File.separator;
		
		File uploadDir = new File(path);
		if(!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		
		String name = image.getOriginalFilename();
		if(name!=null) {
			name = name.replace(" ", "_");			
		} else {
			name = "image.png";
		}
		
		String fileName = System.currentTimeMillis() + "_" + name;
		Path filePath = Paths.get(path, fileName); 
		Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		
		// Build only the path
        String imagePath = UriComponentsBuilder.fromPath(UPLOAD_DIR + File.separator + post.getUserId() + File.separator + fileName)
            .build()
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
	public void deletePost(String id) throws IOException {
		Post post = repo.findById(id).orElseThrow(() -> new PostNotFoundException(id));
		Files.deleteIfExists(Paths.get(post.getImageUrl()));
		repo.deleteById(id);
	}

	private Post requestToModel(RequestPostDto postDto) {
		return modelMapper.map(postDto, Post.class);
	}

	private ResponsePostDto modelToResponse(Post post) {
		return modelMapper.map(post, ResponsePostDto.class);
	}

}
