/**
 * 11-Mar-2025
 */
package com.socio.userservice.exception;

import com.socio.userservice.dto.ExceptionResponse;

/**
 * Throw this runtime exception when another service throws an error
 */
public class ClientServiceException extends RuntimeException {
	private static final long serialVersionUID = 1451470615680796515L;

	private final ExceptionResponse exceptionResponse;

	public ClientServiceException(ExceptionResponse exceptionResponse) {
		super();
		this.exceptionResponse = exceptionResponse;
	}
	
	public ExceptionResponse getExceptionResponse() {
		return exceptionResponse;
	}

}
