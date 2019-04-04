#!/bin/bash
#Variables

if [ -z "$1" ]; then
	echo "Kindly provide stack name to terminate"
else
	stackname=$1
	EC2_ID=$(aws ec2 describe-instances \
	    --query "Reservations[].Instances[].InstanceId[]" \
	    --filters "Name=tag-key,Values=aws:cloudformation:stack-name" "Name=tag-value,Values=$stackname" 			"Name=instance-state-code,Values=16" \
	    --output=text )
	EC2_ID_STATUS=$?
	if [ -z "$EC2_ID" ]; then
		echo "No running EC2 with provided stack"
		exit 1
	else
		echo "ec2id:$EC2_ID"
		if [ $EC2_ID_STATUS -eq 0 ]; then
			echo "Deletion started"
			aws ec2 modify-instance-attribute --instance-id $EC2_ID --no-disable-api-termination
			terminateOutput=$(aws cloudformation delete-stack --stack-name $stackname)
			EC2_TERMINATE_STATUS=$?
			aws cloudformation wait stack-delete-complete --stack-name $stackname
			if [ $EC2_TERMINATE_STATUS -eq 0 ]; then
				echo "Stack $stackname deleted successfully"
			else
				echo "Cannot terminate ec2: $EC2_ID due to $terminateOutput"
				echo " Termination failed "
				exit 1
			fi;
		else
			echo "Cannot find EC2 instance for stack: $stackname"
			exit 1
		fi
	fi;

fi;
