/**
 * 03-Mar-2025
 */
package com.socio.chatservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.socio.chatservice.dto.ExceptionResponse;
import com.socio.chatservice.exception.MessageNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller to handle exceptions
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

	/**
	 * Exception handler for Message Not Found
	 * 
	 * @param e {@link MessageNotFoundException}
	 * @return {@link ExceptionResponse} with status 404
	 */
	@ExceptionHandler(MessageNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleMessageNotFoundException(MessageNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionGenerator((short) 404, "Message not found", e.getMessage()),
				HttpStatus.NOT_FOUND);
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
