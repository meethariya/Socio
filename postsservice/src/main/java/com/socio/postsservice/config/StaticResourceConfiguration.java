/**
 * 22-Mar-2025
 */
package com.socio.postsservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Handle assets uploaded by users.
 */
@Configuration
public class StaticResourceConfiguration implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:/uploads/");
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}
}
