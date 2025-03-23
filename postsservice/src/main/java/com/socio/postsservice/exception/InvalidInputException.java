/**
 * 23-Mar-2025
 * meeth
 */
package com.socio.postsservice.exception;

/**
 * Runtime exception for when input by user is not valid.
 */
public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = 910537345293398344L;

	public InvalidInputException() {
		super();
	}

	public InvalidInputException(String message) {
		super(message);
	}
	
}
