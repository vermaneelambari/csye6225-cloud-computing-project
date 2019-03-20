package com.cloud.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timgroup.statsd.StatsDClient;

@Service
public class CloudwatchServiceImpl implements CloudwatchService{
	
	private StatsDClient statsDClient;
	
	@Autowired
	public CloudwatchServiceImpl(StatsDClient statsDClient) {
		this.statsDClient = statsDClient;
	}

	@Override
	public StatsDClient metricsClient() {

		return statsDClient;
	}

}
