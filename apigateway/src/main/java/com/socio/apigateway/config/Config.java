/**
 * 23-May-2025
 */
package com.socio.apigateway.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

import brave.propagation.B3Propagation;
import brave.propagation.Propagation.Factory;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Config
 */
@Configuration
@AllArgsConstructor
public class Config {

	private final StandardEnvironment environment;

	/**
	 * Continue same trace id from UI by using the B3 header
	 * 
	 * @return {@link B3Propagation}
	 */
	@Bean
	Factory propagationFactory() {
		return B3Propagation.newFactoryBuilder().injectFormat(B3Propagation.Format.SINGLE) // support "b3: ..." style
				.build();
	}

	@Bean
	KeyResolver ipKeyResolver() {
		return exchange -> {
			String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
			return Mono.just(ip);
		};
	}

	@PostConstruct
	public void init() {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		Map<String, Object> envVars = new HashMap<>();
		dotenv.entries().forEach(entry -> envVars.put(entry.getKey(), entry.getValue()));

		MutablePropertySources propertySources = environment.getPropertySources();
		propertySources.addFirst(new MapPropertySource("dotenvProperties", envVars));
	}

}
