package com.socio.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

import brave.propagation.B3Propagation;
import brave.propagation.Propagation.Factory;
import feign.Capability;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServerApplication.class, args);
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

}
