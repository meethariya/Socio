/**
 * 07-Apr-2025
 */
package com.socio.chatservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import brave.propagation.B3Propagation;
import brave.propagation.Propagation.Factory;
import feign.Capability;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * Configuration for Websocket and other beans
 */
@Configuration
@EnableWebSocketMessageBroker
public class Config implements WebSocketMessageBrokerConfigurer{

	/**
	 * Bean for Model Mapper
	 * @return {@link ModelMapper}
	 */
	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
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
	
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
        	.addEndpoint("/ws")
			.setAllowedOriginPatterns("*")
			.withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Use an in-memory message broker for simplicity; for huge loads, consider a dedicated broker.
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
    
	@Bean
	NewTopic newTopic() {
		return new NewTopic("chat-messages", 3, (short) 1);
	}
}
