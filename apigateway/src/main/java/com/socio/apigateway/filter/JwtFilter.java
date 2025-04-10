/**
 * 13-Mar-2025
 */
package com.socio.apigateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;

import javax.crypto.SecretKey;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socio.apigateway.dto.ExceptionResponse;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import reactor.core.publisher.Mono;

/**
 * Auth filter for the gateway
 */
@Component
public class JwtFilter implements GlobalFilter, Ordered{

	private String secret;
	
	private final ObjectMapper objectMapper;
	private static final String UNAUTHORIZED = "Unauthorized";
	
	public JwtFilter(ObjectMapper mapper) {
		this.objectMapper = mapper;
		this.secret = Dotenv.load().get("JWT.secret");
	}
	
	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		// if route is open, don't validate it
		if(this.isOpenRoute.test(exchange.getRequest())) return chain.filter(exchange);
		
		// if request is missing Bearer token, return as UNAUTHORIZED
		List<String> auth = exchange.getRequest().getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION);
		if(auth.isEmpty() || !auth.get(0).startsWith("Bearer "))
			return setExceptionResponse(exchange, UNAUTHORIZED, "No token found", 401);
		
		// validate token
		String token = auth.get(0).substring(7);
		try {
			validateToken(token);
		} catch(ExpiredJwtException e) {
			return setExceptionResponse(exchange, UNAUTHORIZED, "Token is expired", 401);
		} catch (SignatureException e) {
			return setExceptionResponse(exchange, UNAUTHORIZED, "Token is tampered and it's integrity is compromised!", 401);
		} catch (MalformedJwtException e) {
			return setExceptionResponse(exchange, UNAUTHORIZED, "Token is invalid or corrupted", 401);
		} catch (JwtException e) {
			return setExceptionResponse(exchange, UNAUTHORIZED, e.getMessage(), 401);
		}
		
		// valid token
		return chain.filter(exchange);
	}
	
	/**
	 * Validate JWT. If invalid throws exceptions
	 * @param token JWT
	 */
	private void validateToken(String token) {
		Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
	}
	
	/**
	 * Get secret key from JWT secret
	 * @return signing key
	 */
	private SecretKey getSignKey() {
		byte[] signKey = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(signKey);
	}
	
	/**
	 * Predicate to decide if request is for open route or authorized.
	 */
	private Predicate<ServerHttpRequest> isOpenRoute = req -> {
		String path = req.getURI().getPath();
		String method = req.getMethod().name();
		return (
				path.startsWith("/ws") ||
				path.equals("/user") && method.equals("POST") ||
				path.startsWith("/user/by-username/") && method.equals("GET") ||
				path.startsWith("/user/profile-summary/") && method.equals("GET") ||
				path.equals("/auth/generate-token") && method.equals("POST")  ||
				path.equals("/auth/validate-token") && method.equals("GET")  ||
				path.startsWith("/uploads/") && method.equals("GET")
		);
	};
	
	/**
	 * Prepare the standard response for exception
	 * @param exchange {@link ServerWebExchange}
	 * @param error exception error title
	 * @param detail exception details
	 * @param status status code
	 * @return {@link ExceptionResponse}
	 */
	private Mono<Void> setExceptionResponse(ServerWebExchange exchange, String error, String detail, int status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.valueOf(status));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ExceptionResponse exceptionResponse = new ExceptionResponse((short)status, error, detail);
        
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(exceptionResponse);
        } catch (JsonProcessingException e) {
            // Fallback message if conversion fails
            bytes = ("{\"timestamp\":\"" + System.currentTimeMillis() + "\",\"status\":401,\"error\":\"" 
                     + error + "\",\"detail\":\"" + detail + "\"}").getBytes(StandardCharsets.UTF_8);
        }
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

}
