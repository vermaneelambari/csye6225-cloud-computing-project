#!/bin/bash
#Variables

if [ $# -ne 1 ]; then
	echo "please enter valid stack name with filename"
else
	MYSTACK=$1
	stack_ID=$(aws cloudformation describe-stacks \
	--stack-name $MYSTACK)
	stack_ID_STATUS=$?
	if [ $stack_ID_STATUS -eq 0 ]; then
		displayOutput=$(aws cloudformation delete-stack --stack-name $MYSTACK)
		if [ $? -eq 0 ]; then
			echo "Deleting Stack now"
			aws cloudformation wait stack-delete-complete --stack-name $MYSTACK
			echo $terminateOutput
			echo "Successfully deleted " $1
		else
			echo "Deletion failed"
			exit $displayOutput
		fi
        else

		exit "stack does not exists"
	fi
fi
