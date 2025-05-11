/**
 * 11-May-2025
 */
package com.socio.userservice.exception;

/**
 * Throw when exception occurs during Async exceptions on different threads
 */
public class AsyncException extends RuntimeException {

	private static final long serialVersionUID = 8152489195394737010L;

	public AsyncException() {
		super();
	}

	public AsyncException(String message) {
		super(message);
	}
	
	public AsyncException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
