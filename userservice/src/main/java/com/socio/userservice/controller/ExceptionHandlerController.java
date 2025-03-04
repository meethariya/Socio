/**
 * 03-Mar-2025
 */
package com.socio.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.socio.userservice.dto.ExceptionResponse;
import com.socio.userservice.exception.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller to handle exceptions
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionGenerator((short)404, "User not found", e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleUserException(Exception e) {
		log.error(e.getMessage(), e.fillInStackTrace());
		return new ResponseEntity<>(exceptionGenerator((short)500, "Unkown exception occured", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ExceptionResponse exceptionGenerator(short status, String error, String detail) {
		return new ExceptionResponse(status, error, detail);
	}
}
