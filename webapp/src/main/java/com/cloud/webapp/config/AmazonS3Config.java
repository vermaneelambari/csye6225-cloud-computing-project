package com.cloud.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;


@Configuration
@Profile("prod")
public class AmazonS3Config {
	
//	@Value("${aws.access.key.id}")
//	private String awsKeyId;
//	
//	@Value("${aws.access.key.secret}")
//	private String awsKeySecret;
//	
	@Value("${aws.region}")
	private String awsRegion;
	
	@Value("${aws.s3.audio.bucket}")
	private String awsS3AudioBucket;
//	
	@Bean(name = "awsCredentialsProvider")
	public AWSCredentialsProvider getAWSCredentials() {
		//BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.awsKeyId, this.awsKeySecret);
		
		return new InstanceProfileCredentialsProvider(true);
	}
//
//	@Bean(name = "awsKeyId")
//	public String getAwsKeyId() {
//		return awsKeyId;
//	}
//
//	@Bean(name = "awsKeySecret")
//	public String getAwsKeySecret() {
//		return awsKeySecret;
//	}
//
	@Bean(name = "awsRegion")
	public Region getAwsRegion() {
		return Region.getRegion(Regions.fromName(awsRegion));
	}

	@Bean(name = "awsS3AudioBucket")
	public String getAwsS3AudioBucket() {
		return awsS3AudioBucket;
	}
	
	@Value("${metrics.publish}")
	public boolean metricsPublish;
	
	@Value("${metrics.server.hostname}")
	public String metricsHostName;
	
	@Value("${metrics.server.port}")
	public int metricsPort;
	
	@Bean(name = "metricsClient")
	public StatsDClient statsDClient() {
		if(metricsPublish) {
			return new NonBlockingStatsDClient("csye6225", metricsHostName, metricsPort);
		}
		
		return new NoOpStatsDClient();
	}
	
	

}
