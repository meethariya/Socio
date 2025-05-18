/**
 * 09-Mar-2025
 */
package com.socio.authservice.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import brave.propagation.B3Propagation;
import brave.propagation.Propagation.Factory;
import feign.Capability;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * Configuration for security and additional beans
 */
@Configuration
public class ConfigManager {

	/**
	 * Bean for model mapper
	 * 
	 * @return {@link ModelMapper}
	 */
	@Bean
	ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper;
	}
	
	/**
	 * Bean for {@link MicrometerCapability}
	 * 
	 * @param registry
	 * @return {@link MicrometerCapability}
	 */
	@Bean
	Capability capability(final MeterRegistry registry) {
		return new MicrometerCapability(registry);
	}
	
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

	/**
	 * Bean for password hasher
	 * 
	 * @return {@link BCryptPasswordEncoder}
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Authentication manager
	 * 
	 * @param authConfig config
	 * @return {@link AuthenticationManager}
	 * @throws Exception
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * Security filter chain
	 * 
	 * @param http request
	 * @return {@link SecurityFilterChain}
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(withDefaults()).csrf(csrf -> csrf.disable()).cors(withDefaults())
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
