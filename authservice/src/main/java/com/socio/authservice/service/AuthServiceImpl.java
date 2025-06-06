/**
 * 09-Mar-2025
 */
package com.socio.authservice.service;

import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socio.authservice.dto.AuthRequest;
import com.socio.authservice.dto.AuthResponse;
import com.socio.authservice.model.Auth;
import com.socio.authservice.repository.AuthRepository;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service layer for auth
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

	private AuthRepository repository;

	private ModelMapper mapper;

	private PasswordEncoder encoder;

	private String secret;

	public AuthServiceImpl(AuthRepository repository, ModelMapper mapper, PasswordEncoder encoder) {
		this.repository = repository;
		this.mapper = mapper;
		this.encoder = encoder;
		this.secret = Dotenv.load().get("JWT.secret");
	}

	@Override
	public AuthResponse createAuth(AuthRequest request) {
		Optional<Auth> byUsername = repository.findByUsername(request.getUsername());
		if (byUsername.isPresent()) {
			throw new DataIntegrityViolationException("Username: "+ request.getUsername() +" is already taken");
		}
		
		Auth auth = requestToModel(request);
		auth.setPassword(encoder.encode(request.getPassword()));
		return modelToResponse(repository.save(auth));
	}

	@Override
	public String generateToken(AuthRequest request) {
		Auth details = loadUserByUsername(request.getUsername());
		if (!encoder.matches(request.getPassword(), details.getPassword())) {
			throw new BadCredentialsException("Invalid username or password");
		}
		return generateToken(details.getId());
	}

	@Override
	public AuthResponse changePassword(long id, AuthRequest request) {
		Auth details = repository.findById(id).orElseThrow(()->new BadCredentialsException("Account does not exists"));
		if (!encoder.matches(request.getPassword(), details.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}
		details.setPassword(encoder.encode(request.getNewPassword()));
		return modelToResponse(repository.save(details));
	}

	@Override
	public long validateToken(String token) {
		Jws<Claims> signedClaims = Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
		return Long.valueOf(signedClaims.getPayload().getSubject());
	}

	@Override
	public void deleteAuth(String username) {
		repository.deleteByUsername(username);
	}

	@Override
	public AuthResponse getAuth(String username) {
		return modelToResponse(loadUserByUsername(username));
	}
	
	@Override
	public Auth loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));
	}

	private String generateToken(Long userId) {
		return Jwts.builder().subject(userId.toString()).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + (12 * 60 * 60 * 1000))) // 12hrs
				.signWith(getSignKey()).claim("role", "USER").compact();
	}

	private SecretKey getSignKey() {
		byte[] signKey = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(signKey);
	}

	private Auth requestToModel(AuthRequest authRequest) {
		return mapper.map(authRequest, Auth.class);
	}

	private AuthResponse modelToResponse(Auth auth) {
		return mapper.map(auth, AuthResponse.class);
	}

}
