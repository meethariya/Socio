/**
 * 03-Mar-2025
 */
package com.socio.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.socio.userservice.dto.ExceptionResponse;
import com.socio.userservice.exception.DuplicateFriendshipException;
import com.socio.userservice.exception.FriendshipNotFoundException;
import com.socio.userservice.exception.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller to handle exceptions
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

	/**
	 * Exception handler for User Not Found
	 * @param e {@link UserNotFoundException}
	 * @return {@link ExceptionResponse} with status 404
	 */
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionGenerator((short)404, "User not found", e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Exception handler for Friendship Not Found
	 * @param e {@link FriendshipNotFoundException}
	 * @return {@link ExceptionResponse} with status 404
	 */
	@ExceptionHandler(FriendshipNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleFriendshipNotFoundException(FriendshipNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionGenerator((short)404, "Friend not found", e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Exception handler for Duplicate friend request
	 * @param e {@link DuplicateFriendshipException}
	 * @return {@link ExceptionResponse} with status 409
	 */
	@ExceptionHandler(DuplicateFriendshipException.class)
	public ResponseEntity<ExceptionResponse> handleDuplicateFriendShipException(DuplicateFriendshipException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionGenerator((short)409, "Duplicate request", e.getMessage()), HttpStatus.CONFLICT);
	}
	
	/**
	 * Exception handler for generic Exception
	 * @param e {@link Exception}
	 * @return {@link ExceptionResponse} with status 500
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleUserException(Exception e) {
		log.error(e.getMessage(), e.fillInStackTrace());
		return new ResponseEntity<>(exceptionGenerator((short)500, "Unkown exception occured", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Create exception response
	 * @param status status code
	 * @param error error title
	 * @param detail error description
	 * @return {@link ExceptionResponse}
	 */
	private ExceptionResponse exceptionGenerator(short status, String error, String detail) {
		return new ExceptionResponse(status, error, detail);
	}
}
