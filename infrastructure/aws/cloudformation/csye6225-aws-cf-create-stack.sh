#!/bin/bash
#Variables

STACKNAME=$1
VPC_CIDR=$2
SUBNET_1_NAME=$3
SUBNET_1_CIDR=$4
SUBNET_1_AZ=$5
SUBNET_2_NAME=$6
SUBNET_2_CIDR=$7
SUBNET_2_AZ=$8
SUBNET_3_NAME=$9
SUBNET_3_CIDR=${10}
SUBNET_3_AZ=${11}

if [ $# -ne 11 ]; then
	echo "--------------------------Error in Parameters--------------------------"
	echo -e "You need to provide following parameters in the same order as follows:\n\
Stack\n
Vpc CIDR\n\
Subnet1 Name\nSubnet1 CIDR\nSubnet1 AZ\n\
Subnet2 Name\nSubnet2 CIDR\nSubnet2 AZ\n\
Subnet3 Name\nSubnet3 CIDR\nSubnet3 AZ"
exit 1
fi
echo "----------------You have provided following paramters----------------"

echo -e "Stack - $STACKNAME \n, VPC CIDR - $VPC_CIDR \n\
Subnet1 Name - $SUBNET_1_NAME, Subnet1 CIDR - $SUBNET_1_CIDR, Subnet1 AZ - $SUBNET_1_AZ\n\
Subnet2 Name - $SUBNET_2_NAME, Subnet2 CIDR - $SUBNET_2_CIDR, Subnet2 AZ - $SUBNET_2_AZ\n\
Subnet3 Name - $SUBNET_3_NAME, Subnet3 CIDR - $SUBNET_3_CIDR, Subnet3 AZ - $SUBNET_3_AZ"

createOutput=$(aws cloudformation create-stack --stack-name $STACKNAME --template-body file://csye6225-cf-networking.yaml --parameters ParameterKey=stackname,ParameterValue=$STACKNAME ParameterKey=myVpcCIDR,ParameterValue=$VPC_CIDR ParameterKey=Subnet1,ParameterValue=$SUBNET_1_NAME ParameterKey=subnet1CIDR,ParameterValue=$SUBNET_1_CIDR ParameterKey=Subnet1AZ,ParameterValue=$SUBNET_1_AZ ParameterKey=Subnet2,ParameterValue=$SUBNET_2_NAME ParameterKey=subnet2CIDR,ParameterValue=$SUBNET_2_CIDR ParameterKey=Subnet2AZ,ParameterValue=$SUBNET_2_AZ ParameterKey=Subnet3,ParameterValue=$SUBNET_3_NAME ParameterKey=subnet3CIDR,ParameterValue=$SUBNET_3_CIDR ParameterKey=Subnet3AZ,ParameterValue=$SUBNET_3_AZ --output text)
createOutput_status=$?
if [ $createOutput_status -eq 0 ]; then
	echo "Creating stack..."
	aws cloudformation wait stack-create-complete --stack-name $STACKNAME
	echo "Stack created successfully. Stack Id below: "

	echo $createOutput

else
	echo -e "Error in creation of stack\n"
	exit $createOutput_status
fi
