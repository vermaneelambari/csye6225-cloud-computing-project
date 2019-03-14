STACK_NAME=$1

CODEDEPLOYEC2S3POLICYNAME="CodeDeploy-EC2-S3"
CODEDEPLOYSERVICEROLENAME="CodeDeploySerivceRole"
CIRCLECIUSER="circleci"
CODEDEPLOYAPPNAME="csye6225-webapp"
CIRCLECIUPLOADTOS3POLICYNAME="CircleCI-Upload-To-S3"
CIRCLECICODEDEPLOYPOLICYNAME="CirlceCI-Code-Deploy"
CODEDEPLOYEC2SERVICEROLENAME="CodeDeployEC2ServiceRole"
CIRCLECIEC2AMIPOLICYNAME="circleci-ec2-ami"
AWSREGION="us-east-1"

export AWSACCOUNTID=$(aws sts get-caller-identity --query "Account" --output text)
echo $AWSACCOUNTID

DOMAINNAME=$(aws route53 list-hosted-zones --query HostedZones[0].Name --output text)
DNS=${DOMAINNAME::-1}
echo $DNS

CODEDEPLOYS3BUCKETNAME="code-deploy.${DNS}"
echo $CODEDEPLOYS3BUCKETNAME
BUCKETNAME="${DNS}.csye6225.com"
echo $BUCKETNAME
aws cloudformation create-stack --stack-name $STACK_NAME --capabilities "CAPABILITY_NAMED_IAM" --template-body file://csye6225-cf-cicd.json --parameters ParameterKey=CodeDeployEC2ServiceRoleName,ParameterValue=$CODEDEPLOYEC2SERVICEROLENAME ParameterKey=CircleCiUploadtoS3PolicyName,ParameterValue=$CIRCLECIUPLOADTOS3POLICYNAME ParameterKey=CircleCiUser,ParameterValue=$CIRCLECIUSER ParameterKey=CodeDeployS3BucketName,ParameterValue=$CODEDEPLOYS3BUCKETNAME ParameterKey=CodeDeployApplicationName,ParameterValue=$CODEDEPLOYAPPNAME ParameterKey=AWSRegion,ParameterValue=$AWSREGION ParameterKey=AWSAccountID,ParameterValue=$AWSACCOUNTID ParameterKey=CodeDeployServiceRoleName,ParameterValue=$CODEDEPLOYSERVICEROLENAME ParameterKey=CodeDeployEC2S3PolicyName,ParameterValue=$CODEDEPLOYEC2S3POLICYNAME ParameterKey=CircleCiCodeDeployPolicyName,ParameterValue=$CIRCLECICODEDEPLOYPOLICYNAME ParameterKey=CircleCiEC2AMIPolicyName,ParameterValue=$CIRCLECIEC2AMIPOLICYNAME ParameterKey=bucketname,ParameterValue=$BUCKETNAME 

export STACK_STATUS=$(aws cloudformation describe-stacks --stack-name $STACK_NAME --query "Stacks[][ [StackStatus ] ][]" --output text)

while [ $STACK_STATUS != "CREATE_COMPLETE" ]
do
  STACK_STATUS=`aws cloudformation describe-stacks --stack-name $STACK_NAME --query "Stacks[][ [StackStatus ] ][]" --output text`
done
echo "Created Stack ${STACK_NAME} successfully!"
