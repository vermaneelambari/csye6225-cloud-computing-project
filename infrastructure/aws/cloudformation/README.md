## Instructions to Run Shell Script to Setup AWS infrastructure with AWS CloudFormation:
- The setup script is provided in the file csye-aws-cloudformation.sh
- Open Terminal and Navigate to infrastructure/aws/cloud_formation folder that has the above script file and run the script
- Following parameters are expected while running the script: [stack name] [vpc cidr] [subnet1 name] [subnet1 cidr] [subnet1 availability zone] [subnet2 name] [subnet1 cidr] [subnet2 availability zone] [subnet3 name] [subnet3 cidr] [subnet3 availability zone] 

Example command: ./csye6225-aws-cf-create-stack.sh myStack 10.0.0.0/16 subnet1 10.0.1.0/24 us-east-1a subnet2 10.0.2.0/24 us-east-1b subnet3 10.0.3.0/24 us-east-1c 

## Instructions to Run Shell Script to Teardown AWS infrastructure with AWS CloudFormation:
- The teardown script is provided in the file csye6225-aws-cf-terminate-stack.sh
- Open Terminal and Navigate to infrastructure/aws/cloud_formation folder that has the above script file and run the script
- Following parameters are expected while running the script: [stack name]

Example command: ./csye6225-aws-cf-terminate-stack.sh myStack


## Instructions to Run Shell Script to Setup CICD Stack with AWS CloudFormation:
- This stack is used to created circleCI user and related policies and roles
- The application script is provided in the file csye6225-aws-cf-create-cicd-stack.sh
- Open Terminal and Navigate to infrastructure/aws/cloud_formation folder that has the above script file and run the script
- Following parameters are expected while running the script:[cicd_stack_name]

Example command: ./csye6225-aws-cf-create-cicd-stack.sh cicdStack

## Instructions to Run Shell Script to Teardown CICD Stack with AWS CloudFormation:
- The teardown script is provided in the file csye6225-aws-cf-terminate-cicd-stack.sh
- Open Terminal and Navigate to infrastructure/aws/cloud_formation folder that has the above script file and run the script
- Following parameters are expected while running the script: [cicd_stack_name name]

Example command: ./csye6225-aws-cf-terminate-application-stack.sh cicdStack

## Instructions to Run Shell Script to Setup AWS Application with AWS CloudFormation:
- The application script is provided in the file csye6225-aws-cf-create-application-stack.sh
- Open Terminal and Navigate to infrastructure/aws/cloud_formation folder that has the above script file and run the script
- Following parameters are expected while running the script: [web stack name] [stack_name] [cicd_stack_name]

Example command: ./csye6225-aws-cf-create-application-stack.sh webStack myStack cicdStack

## Instructions to Run Shell Script to Teardown AWS Application with AWS CloudFormation:
- The teardown script is provided in the file csye6225-aws-cf-terminate-application-stack.sh
- Open Terminal and Navigate to infrastructure/aws/cloud_formation folder that has the above script file and run the script
- Following parameters are expected while running the script: [webstack name]

Example command: ./csye6225-aws-cf-terminate-application-stack.sh webStack
