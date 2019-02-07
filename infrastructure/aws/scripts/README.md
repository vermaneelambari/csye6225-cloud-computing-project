## Instructions to Run Shell Script to Setup AWS infrastructure with AWS CLI:
- The setup script is provided in the file csye6225-aws-networking-setup.sh
- Open Terminal and Navigate to infrastructure/aws/scripts folder that has the above script file and run the script
- Following parameters are expected while running the script: [region] [vpc name] [vpc cidr] [subnet1 name] [subnet1 cidr] [subnet1 availability zone] [subnet2 name] [subnet1 cidr] [subnet2 availability zone] [subnet3 name] [subnet3 cidr] [subnet3 availability zone] [internet gateway name] [route table name]

Example command: ./csye6225-aws-networking-setup.sh us-east-1 myVpc 10.0.0.0/16 subnet1 10.0.1.0/24 us-east-1a subnet2 10.0.2.0/24 us-east-1b subnet3 10.0.3.0/24 us-east-1c myInternetGateway myRouteTable

## Instructions to Run Shell Script to Teardown AWS infrastructure with AWS CLI:
- The teardown script is provided in the file csye6225-aws-networking-teardown.sh
- Open Terminal and Navigate to infrastructure/aws/scripts folder that has the above script file and run the script
- Following parameters are expected while running the script: [vpc name]

Example command: ./csye6225-aws-networking-teardown.sh myVpc
