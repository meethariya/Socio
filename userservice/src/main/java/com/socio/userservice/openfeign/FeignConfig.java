/**
 * 11-Mar-2025
 */
package com.socio.userservice.openfeign;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socio.userservice.dto.ExceptionResponse;
import com.socio.userservice.exception.ClientServiceException;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

/**
 * Error decoder config for feign client
 */
@Configuration
public class FeignConfig {

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Bean
	ErrorDecoder errorDecoder() {
		return (String methodKey, Response response) -> {
			HttpStatus status = HttpStatus.valueOf(response.status());
			if(response.body() == null)
				throw new ClientServiceException(exceptionGenerator((short)status.value(), "Invalid response", "No response from client service"));
			String stringBody="";
			try {
				stringBody = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
			} catch (IOException e) {
				throw new ClientServiceException(exceptionGenerator((short)status.value(), "Unable to decode response", e.getMessage()));
			}
			try {
				ExceptionResponse exceptionResponse = objectMapper.readValue(stringBody, ExceptionResponse.class);
				throw new ClientServiceException(exceptionResponse);
			} catch (JsonProcessingException e) {
				throw new ClientServiceException(exceptionGenerator((short)status.value(), "Unable to decode error response", stringBody));
			}
		};
	}
	
	/**
	 * Create exception response
	 * @param status status code
	 * @param error error title
	 * @param detail error description
	 * @return {@link ExceptionResponse}
	 */
	private ExceptionResponse exceptionGenerator(short status, String error, String detail) {
		return new ExceptionResponse(status, error, detail);
	}
}
