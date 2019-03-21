#!/bin/bash

cd /opt/aws/amazon-cloudwatch-agent/bin/
sudo ./amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/opt/aws/amazon-cloudwatch-agent/bin/cloudwatch-config.json -s

sudo systemctl daemon-reload
sudo systemctl start amazon-cloudwatch-agent.service
sudo systemctl stop amazon-cloudwatch-agent.service
sudo systemctl restart amazon-cloudwatch-agent.service

#Start application
sudo systemctl start tomcat.service
