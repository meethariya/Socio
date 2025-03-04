/**
 * 03-Mar-2025
 */
package com.socio.userservice.exception;

/**
 * Runtime Exception for User not found.
 */
public class UserNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -3845508851414395994L;

	public UserNotFoundException() {
		super("User not found");
	}

	public UserNotFoundException(String message) {
		super(message);
	}
	
	public UserNotFoundException(long id) {
		super("User not found with id: "+id);
	}
}
