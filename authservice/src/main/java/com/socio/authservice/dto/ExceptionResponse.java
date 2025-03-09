/**
 * 03-Mar-2025
 */
package com.socio.authservice.dto;

import java.util.Date;

import lombok.Data;

/**
 * Standard response to throw exceptions
 */
@Data
public class ExceptionResponse {

	private Date timestamp;
	private short status;
	private String error;
	private String detail;

	public ExceptionResponse(short status, String error, String detail) {
		this.timestamp = new Date();
		this.status = status;
		this.error = error;
		this.detail = detail;
	}

}
