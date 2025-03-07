/**
 * 06-Mar-2025
 */
package com.socio.userservice.exception;

/**
 * Runtime execption for friendship not found
 */
public class FriendshipNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5114175546882509161L;

	public FriendshipNotFoundException() {
		super();
	}

	public FriendshipNotFoundException(String message) {
		super(message);
	}
	
	public FriendshipNotFoundException(long id) {
		super("No friend relation found with id: "+id);
	}
	
}
