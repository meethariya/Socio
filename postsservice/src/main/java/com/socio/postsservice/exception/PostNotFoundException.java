/**
 * 04-Mar-2025
 */
package com.socio.postsservice.exception;

/**
 * Runtime Exception for Post not found
 */
public class PostNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5948972670969088520L;

	public PostNotFoundException() {
		super("Post not found");
	}

	public PostNotFoundException(String message) {
		super("Post not found with id: "+message);
	}

}
