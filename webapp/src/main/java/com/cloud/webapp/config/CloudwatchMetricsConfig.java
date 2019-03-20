package com.cloud.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

@Configuration
@Profile("prod")
public class CloudwatchMetricsConfig {

	@Value("${metrics.publish}")
	public boolean metricsPublish;
	
	@Value("${metrics.server.hostname}")
	public String metricsHostName;
	
	@Value("${metrics.server.port}")
	public int metricsPort;
	
	@Bean(name="metricsClient")
	public StatsDClient metricsClient() {
		if(metricsPublish) {
			return new NonBlockingStatsDClient("csye6225", metricsHostName, metricsPort);
		}
		
		return new NoOpStatsDClient();
	}
}
