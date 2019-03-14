STACK_NAME=$1


stack_ID=$(aws cloudformation describe-stacks \
	--stack-name $STACK_NAME)
stack_ID_STATUS=$?
if [ $stack_ID_STATUS -eq 0 ]; then
    aws cloudformation delete-stack --stack-name $STACK_NAME

    aws cloudformation wait stack-delete-complete --stack-name $STACK_NAME
fi
if [ $? -ne "0" ]
then 
	echo "Deletion of Stack failed"
else
	echo "Deletion of Stack Success"
fi

