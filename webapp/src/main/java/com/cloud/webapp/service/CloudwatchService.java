package com.cloud.webapp.service;

import com.timgroup.statsd.StatsDClient;

public interface CloudwatchService {

	StatsDClient metricsClient();
}
