/**
 * 04-Mar-2025
 */
package com.socio.postsservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.socio.postsservice.dto.ExceptionResponse;
import com.socio.postsservice.exception.CommentNotFoundException;
import com.socio.postsservice.exception.InvalidInputException;
import com.socio.postsservice.exception.PostNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller to handle exceptions
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

	@Value("${spring.servlet.multipart.max-file-size}")
	private String fileMaxValue;
	
	/**
	 * Exception handler for Post Not Found
	 * 
	 * @param e {@link PostNotFoundException}
	 * @return {@link ExceptionResponse} with status 404
	 */
	@ExceptionHandler(PostNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handlePostNotFoundException(PostNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(responseGenerator((short) 404, "Post not found", e.getMessage()),
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Exception handler for Comment Not Found
	 * 
	 * @param e {@link CommentNotFoundException}
	 * @return {@link ExceptionResponse} with status 404
	 */
	@ExceptionHandler(CommentNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleCommentNotFoundException(CommentNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(responseGenerator((short) 404, "Comment not found", e.getMessage()),
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Exception handler for Invalid User Input
	 * 
	 * @param e {@link PostNotFoundException}
	 * @return {@link ExceptionResponse} with status 422
	 */
	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<ExceptionResponse> handleInvalidInputException(InvalidInputException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(responseGenerator((short) 422, "Invalid User Input", e.getMessage()),
				HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	/**
	 * Exception handler for Invalid User Input
	 * 
	 * @param e {@link PostNotFoundException}
	 * @return {@link ExceptionResponse} with status 422
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ExceptionResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(responseGenerator((short) 422, "File too Big! Should not exceed "+fileMaxValue, e.getMessage()),
				HttpStatus.UNPROCESSABLE_ENTITY);
	}

	/**
	 * Exception handler for generic Exception
	 * 
	 * @param e {@link Exception}
	 * @return {@link ExceptionResponse} with status 500
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception e) {
		log.error(e.getMessage(), e.fillInStackTrace());
		return new ResponseEntity<>(responseGenerator((short) 500, "Unexpected error occured", e.getMessage()),
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
	private ExceptionResponse responseGenerator(short status, String error, String detail) {
		return new ExceptionResponse(status, error, detail);
	}
}
