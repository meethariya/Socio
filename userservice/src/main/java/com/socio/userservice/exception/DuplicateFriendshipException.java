/**
 * 07-Mar-2025
 */
package com.socio.userservice.exception;

/**
 * Runtime exception when duplicate friendship request is detected
 */
public class DuplicateFriendshipException extends RuntimeException{

	private static final long serialVersionUID = 5609656553024973051L;

	public DuplicateFriendshipException() {
		super();
	}

	public DuplicateFriendshipException(String message) {
		super(message);
	}
	
	public DuplicateFriendshipException(long id) {
		super("Friend relation with id: "+id+" already exists");
	}
	
}
