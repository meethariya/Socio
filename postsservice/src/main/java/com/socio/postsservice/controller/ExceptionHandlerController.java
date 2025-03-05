/**
 * 04-Mar-2025
 */
package com.socio.postsservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.socio.postsservice.dto.ExceptionResponse;
import com.socio.postsservice.exception.PostNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller to handle exceptions
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

	@ExceptionHandler(PostNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handlePostNotFoundException(PostNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(responseGenerator((short)404, "Post not found", e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception e) {
		log.error(e.getMessage(), e.fillInStackTrace());
		return new ResponseEntity<>(responseGenerator((short)500, "Unexpected error occured", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ExceptionResponse responseGenerator(short status, String error, String detail) {
		return new ExceptionResponse(status, error, detail);
	}
}
