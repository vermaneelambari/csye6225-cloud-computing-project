#!/bin/bash
#Variables

stack_name=$1
net_stack=$2
cicd_stack=$3

if [ $# -ne 3 ]; then
	echo "--------------------------Error in Parameters--------------------------"
	echo -e "You need to provide following parameters in the same order as follows:\n\
	StackName\n Network StackName\n CICD stackName"
exit 1
fi
echo $stack_name
echo $net_stack
sid=$(aws cloudformation describe-stacks --stack-name $net_stack --query Stacks[0].StackId --output text)
if [ -z $sid ]; then
	echo "Other Stack is not running.Please start that Stack"
	exit 1
fi	
echo "Stack Id: $sid"


#ami_id=$(aws ec2 describe-images --filters "Name=name,Values=csye6225*" --query 'sort_by(Images, &CreationDate)[].ImageId' --output text)

ami_id=$(aws ec2 describe-images --filters "Name=name,Values=csye6225*" 'Name=state,Values=available' --query 'sort_by(Images, &CreationDate)[-1].ImageId' --output text)

ami_id_status=$?

echo $ami_id

LAUNCH_CONFIGURATION_NAME=asg_launch_config
echo $LAUNCH_CONFIGURATION_NAME



if [ $ami_id_status -eq 0 ]; then
	DOMAINNAME=$(aws route53 list-hosted-zones --query HostedZones[0].Name --output text)
	DNS=${DOMAINNAME::-1}
	echo $DNS
	bucket_name="${DNS}.csye6225.com"
	echo $bucket_name
	codedeploy_bucketname="code-deploy.${DNS}"
	echo $codedeploy_bucketname
	ec2tagfilter="webappEC2"
	echo $ec2tagfilter
	account_number=$(aws sts get-caller-identity --output text --query 'Account')
	PUBLIC_IP=$(curl ifconfig.me)
	echo $PUBLIC_IP
	WAF_CERTIFICATE_ARN=$(aws acm list-certificates --query "CertificateSummaryList[?DomainName=='${DNS}'].CertificateArn" --output text)
	echo $WAF_CERTIFICATE_ARN
	NOWAF_CERTIFICATE_ARN=$(aws acm list-certificates --query "CertificateSummaryList[?DomainName=='nowaf.${DNS}'].CertificateArn" --output text)
	echo $NOWAF_CERTIFICATE_ARN
	RDSusername="csye6225master"
	RDSpassword="csye6225password"
	CircleCIUser="circleci"
	LambdaRole="LambdaRole"
	CodeDeployEC2ServiceRole="CodeDeployEC2ServiceRole"
	#cdappname="csye6225-webapp"
	#echo $cdappname
	dns_id_status=$? 
	if [ $dns_id_status -eq 0 ]; then
	  
	  createOutput=$(aws cloudformation create-stack --stack-name $stack_name --capabilities CAPABILITY_NAMED_IAM --template-body file://csye6225-cf-auto-scaling-application.json --parameters ParameterKey=RDSusername,ParameterValue=$RDSusername ParameterKey=RDSpassword,ParameterValue=$RDSpassword ParameterKey=CircleCIUser,ParameterValue=$CircleCIUser ParameterKey=LambdaRole,ParameterValue=$LambdaRole ParameterKey=CodeDeployEC2ServiceRole,ParameterValue=$CodeDeployEC2ServiceRole ParameterKey=publicipaddress,ParameterValue=$PUBLIC_IP ParameterKey=NOWAFCertificateArn,ParameterValue=$NOWAF_CERTIFICATE_ARN ParameterKey=WAFCertificateArn,ParameterValue=$WAF_CERTIFICATE_ARN ParameterKey=LaunchConfigurationName,ParameterValue=$LAUNCH_CONFIGURATION_NAME ParameterKey=stackname,ParameterValue=$stack_name ParameterKey=amiid,ParameterValue=$ami_id ParameterKey=netstack,ParameterValue=$net_stack ParameterKey=bucketname,ParameterValue=$bucket_name ParameterKey=ec2tagfilter,ParameterValue=$ec2tagfilter ParameterKey=cicdstack,ParameterValue=$cicd_stack ParameterKey=domain,ParameterValue=$DNS ParameterKey=codedeploybucketname,ParameterValue=$codedeploy_bucketname ParameterKey=accountnumber,ParameterValue=$account_number)
	  echo $createOutput

	  if [ $? -eq 0 ]; then
		  echo "Creating Stack Now"
		  aws cloudformation wait stack-create-complete --stack-name $stack_name
		  echo "successfully created Stack: " $stack_name "with ID: " $createOutput

	  else
		  echo "Error while creating stack"
		  echo $createOutput
		  exit 1
	  fi;
	fi
else
	echo "Image not found"
	exit 1
fi
