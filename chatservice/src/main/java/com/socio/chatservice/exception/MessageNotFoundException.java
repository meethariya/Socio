/**
 * 07-Apr-2025
 */
package com.socio.chatservice.exception;

/**
 * Runtime exception if message is not found
 */
public class MessageNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8639869374942453183L;

	public MessageNotFoundException() {
		super();
	}

	public MessageNotFoundException(String message) {
		super(message);
	}

}
