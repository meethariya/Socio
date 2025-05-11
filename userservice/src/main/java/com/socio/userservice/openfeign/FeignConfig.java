/**
 * 11-Mar-2025
 */
package com.socio.userservice.openfeign;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
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
import feign.form.spring.SpringFormEncoder;

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
			if (response.body() == null)
				throw new ClientServiceException(exceptionGenerator((short) status.value(), "Invalid response",
						"No response from client service"));
			String stringBody = "";
			try {
				stringBody = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
			} catch (IOException e) {
				throw new ClientServiceException(
						exceptionGenerator((short) status.value(), "Unable to decode response", e.getMessage()));
			}
			try {
				ExceptionResponse exceptionResponse = objectMapper.readValue(stringBody, ExceptionResponse.class);
				throw new ClientServiceException(exceptionResponse);
			} catch (JsonProcessingException e) {
				throw new ClientServiceException(
						exceptionGenerator((short) status.value(), "Unable to decode error response", stringBody));
			}
		};
	}

	// Wrap Spring’s message‐converter–backed encoder with the form encoder:
	@Bean
	SpringFormEncoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
		// SpringEncoder handles normal JSON parts
		SpringEncoder springEncoder = new SpringEncoder(converters);
		// FormEncoder adds multipart/form support
		return new SpringFormEncoder(springEncoder);
	}

	/**
	 * Create exception response
	 * 
	 * @param status status code
	 * @param error  error title
	 * @param detail error description
	 * @return {@link ExceptionResponse}
	 */
	private ExceptionResponse exceptionGenerator(short status, String error, String detail) {
		return new ExceptionResponse(status, error, detail);
	}
}
