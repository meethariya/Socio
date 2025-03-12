/**
 * 03-Mar-2025
 */
package com.socio.userservice.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Standard response to throw exceptions
 */
@Data
@NoArgsConstructor
@ToString
public class ExceptionResponse implements Serializable{

	private static final long serialVersionUID = -1267139218020864139L;
	
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
