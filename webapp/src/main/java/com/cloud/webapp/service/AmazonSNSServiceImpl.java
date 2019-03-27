package com.cloud.webapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;

@Service
@Profile("prod")
public class AmazonSNSServiceImpl implements AmazonSNSService {
	
	private AmazonSNSClient snsClient;
	private static final Logger logger = LoggerFactory.getLogger(AmazonSNSServiceImpl.class);
	
	private String snsTopic;
	
	@Autowired
	public AmazonSNSServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String snsTopic) {
		this.snsTopic = snsTopic;
		logger.info("SNS topic is "+ snsTopic);
		snsClient = (AmazonSNSClient) AmazonSNSClientBuilder.standard()
				.withCredentials(awsCredentialsProvider)
				.withRegion(awsRegion.getName())
				.build();
		logger.info("SNS Client built successfully");
	}

	@Override
	public void publish(String email) {
		
		List<Topic> topicList = snsClient.listTopics().getTopics();
		Topic reset = null;
		
		for(Topic topic : topicList) {
			if(topic.getTopicArn().contains(snsTopic)) {
				reset = topic;
			}
		}
		
		if(reset != null) {
			PublishRequest publishRequest = new PublishRequest(reset.getTopicArn(), email);
			PublishResult publishResult = snsClient.publish(publishRequest);
			logger.info("SNS message published with ID: " + publishResult.getMessageId());
		} else {
			logger.warn("Topic: " + snsTopic + "not found");
		}
	}

}
