/**
 * 09-Mar-2025
 */
package com.socio.authservice.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.socio.authservice.dto.ExceptionResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller to handle exceptions
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

	private static final String UNAUTHORIZED = "Unauthorized";

	/**
	 * Exception handler for UsernameNotFound Exception
	 * 
	 * @param e {@link UsernameNotFoundException}
	 * @return {@link ExceptionResponse} with status 404
	 */
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionGenerator((short) 404, "Not Found", e.getMessage()),HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Exception handler for BadCredentials Exception
	 * 
	 * @param e {@link Exception}
	 * @return {@link ExceptionResponse} with status 401
	 */
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionGenerator((short) 401, UNAUTHORIZED, e.getMessage()),
				HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Exception handler for Signature Exception
	 * 
	 * @param e {@link Exception}
	 * @return {@link ExceptionResponse} with status 401
	 */
	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ExceptionResponse> handleSignatureException(SignatureException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionGenerator((short) 401, UNAUTHORIZED, e.getMessage()),
				HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Exception handler for MalformedJwt Exception
	 * 
	 * @param e {@link Exception}
	 * @return {@link ExceptionResponse} with status 401
	 */
	@ExceptionHandler(MalformedJwtException.class)
	public ResponseEntity<ExceptionResponse> handleMalformedJwtException(MalformedJwtException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionGenerator((short) 401, UNAUTHORIZED, e.getMessage()),
				HttpStatus.UNAUTHORIZED);
	}
	
	/**
	 * Exception handler for ExpiredJwt Exception
	 * 
	 * @param e {@link Exception}
	 * @return {@link ExceptionResponse} with status 401
	 */
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ExceptionResponse> handleExpiredJwtException(ExpiredJwtException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionGenerator((short) 401, UNAUTHORIZED, e.getMessage()),
				HttpStatus.UNAUTHORIZED);
	}
	
	/**
	 * Exception handler for DataIntegrityViolation Exception
	 * 
	 * @param e {@link Exception}
	 * @return {@link ExceptionResponse} with status 409
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionGenerator((short) 409, "Invalid Input", e.getMessage()),
				HttpStatus.CONFLICT);
	}

	/**
	 * Exception handler for generic Exception
	 * 
	 * @param e {@link Exception}
	 * @return {@link ExceptionResponse} with status 500
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleUserException(Exception e) {
		log.error(e.getMessage(), e.fillInStackTrace());
		return new ResponseEntity<>(exceptionGenerator((short) 500, "Unkown exception occured", e.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Create exception response
	 * 
	 * @param status status code
	 * @param error  error title
	 * @param detail error description
	 * @return {@link ExceptionResponse}
	 */
	private ExceptionResponse exceptionGenerator(short status, String error, String detail) {
		return new ExceptionResponse(status, error, detail);
	}
}
