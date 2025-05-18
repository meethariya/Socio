/**
 * 10-May-2025
 */
package com.socio.userservice.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import brave.propagation.B3Propagation;
import brave.propagation.Propagation.Factory;
import feign.Capability;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * Creates Executor for Async threads calls.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    Executor taskExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(2);
        exec.setMaxPoolSize(5);
        exec.setQueueCapacity(50);
        exec.setThreadNamePrefix("async-");
        exec.initialize();
        return exec;
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

