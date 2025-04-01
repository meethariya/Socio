/**
 * 01-Apr-2025
 */
package com.socio.postsservice.exception;

/**
 * Runtime Exception for Comment not found
 */
public class CommentNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 6611085455610565401L;

	public CommentNotFoundException() {
		super("Comment not found");
	}

	public CommentNotFoundException(String id) {
		super("Comment not found with id: "+id);
	}

	
}
