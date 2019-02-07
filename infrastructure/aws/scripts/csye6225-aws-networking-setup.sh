#!/bin/bash


REGION=$1
VPC_NAME=$2
VPC_CIDR=$3
SUBNET_1_NAME=$4
SUBNET_1_CIDR=$5
SUBNET_1_AZ=$6
SUBNET_2_NAME=$7
SUBNET_2_CIDR=$8
SUBNET_2_AZ=$9
SUBNET_3_NAME=${10}
SUBNET_3_CIDR=${11}
SUBNET_3_AZ=${12}
INTERNET_GATEWAY_NAME=${13}
ROUTE_TABLE_NAME=${14}

if [ $# -ne 14 ]; then
	echo "--------------------------Error in Parameters--------------------------"
	echo -e "You need to provide following parameters in the same order as follows:\n\
Region\nVpc Name\nVpc CIDR\n\
Subnet1 Name\nSubnet1 CIDR\nSubnet1 AZ\n\
Subnet2 Name\nSubnet2 CIDR\nSubnet2 AZ\n\
Subnet3 Name\nSubnet3 CIDR\nSubnet3 AZ\n\
InternetGateway Name\nRoute Table Name"
exit 1
fi

echo "----------------You have provided following paramters----------------"
echo -e "Region - $REGION \nVPC Name - $VPC_NAME, VPC CIDR - $VPC_CIDR \n\
Subnet1 Name - $SUBNET_1_NAME, Subnet1 CIDR - $SUBNET_1_CIDR, Subnet1 AZ - $SUBNET_1_AZ\n\
Subnet2 Name - $SUBNET_2_NAME, Subnet2 CIDR - $SUBNET_2_CIDR, Subnet2 AZ - $SUBNET_2_AZ\n\
Subnet3 Name - $SUBNET_2_NAME, Subnet3 CIDR - $SUBNET_3_CIDR, Subnet3 AZ - $SUBNET_3_AZ\n\
InternetGateway Name - $INTERNET_GATEWAY_NAME \n\
Route Table name - $ROUTE_TABLE_NAME"

#Create VPC
echo -e "\n\n--------------------------Creating VPC--------------------------\n\n"


VPC_CHECK_NAME=$(aws ec2 describe-vpcs \
        --filter "Name=tag:Name,Values=$VPC_NAME" \
        --query 'Vpcs[*].{id:VpcId}' \
        --output text)


VPC_ID_STATUS=$?

if [ -z $VPC_CHECK_NAME ]; then
		VPC_ID=$(aws ec2 create-vpc \
		--cidr-block $VPC_CIDR \
		--query 'Vpc.{VpcId:VpcId}' \
		--output text \
		--region $REGION)
	VPC_STATUS=$?
	if [ $VPC_STATUS -eq 0 ]; then
		echo "VPC created with ID $VPC_ID in region $REGION."
	else
		echo "Error while creating VPC"
		exit $VPC_STATUS
	fi

	#Create VPC tag for VPC
	echo -e "\n\n-----------------------Adding Tags to VPC-----------------------\n\n"
	VPC_TAG=$(aws ec2 create-tags \
		--resources $VPC_ID \
		--tags "Key=Name,Value=$VPC_NAME")
	VPC_TAG_STATUS=$?
	if [ $VPC_TAG_STATUS -eq 0 ]; then
		echo "VPC tag created with Name '$VPC_NAME' for VPC '$VPC_ID'."
	else
		echo "Error while creating VPC tag"
		exit $VPC_TAG_STATUS
	fi

	#Create Subnet 1
	echo -e "\n\n-----------------------Creating 1st Subnet-----------------------\n\n"
	SUBNET_1_ID=$(aws ec2 create-subnet \
		--vpc-id $VPC_ID \
		--cidr-block $SUBNET_1_CIDR \
		--availability-zone $SUBNET_1_AZ \
		--query 'Subnet.{SubnetId:SubnetId}' \
		--output text \
		--region $REGION)
	SUBNET_1_STATUS=$?
	if [ $SUBNET_1_STATUS -eq 0 ]; then
		echo "Subnet1 created with ID $SUBNET_1_ID for Vpc $VPC_NAME."
	else
		echo "Error while creating Subnet 1"
		exit $SUBNET_1_STATUS
	fi

	#Create Tag for Subnet 1
	echo -e "\n\n-----------------------Adding Tags to Subnet 1-----------------------\n\n"
	SUBNET_1_TAG=$(aws ec2 create-tags \
		--resources $SUBNET_1_ID \
		--tags "Key=Name,Value=$SUBNET_1_NAME")
	SUBNET_1_TAG_STATUS=$?
	if [ $SUBNET_1_TAG_STATUS -eq 0 ]; then
		echo "Subnet 1 tag created with Name '$SUBNET_1_NAME' for VPC '$VPC_NAME'."
	else
		echo "Error while creating Subnet 1 tag"
		exit $SUBNET_1_TAG_STATUS
	fi

	#Create Subnet 2
	echo -e "\n\n-----------------------Creating 2nd Subnet-----------------------\n\n"
	SUBNET_2_ID=$(aws ec2 create-subnet \
		--vpc-id $VPC_ID \
		--cidr-block $SUBNET_2_CIDR \
		--availability-zone $SUBNET_2_AZ \
		--query 'Subnet.{SubnetId:SubnetId}' \
		--output text \
		--region $REGION)
	SUBNET_2_STATUS=$?
	if [ $SUBNET_2_STATUS -eq 0 ]; then
		echo "Subnet1 created with ID $SUBNET_2_ID for Vpc $VPC_NAME."
	else
		echo "Error while creating Subnet 1"
		exit $SUBNET_2_STATUS
	fi

	#Create Tag for Subnet 2
	echo -e "\n\n-----------------------Adding Tags to Subnet 2-----------------------\n\n"
	SUBNET_2_TAG=$(aws ec2 create-tags \
		--resources $SUBNET_2_ID \
		--tags "Key=Name,Value=$SUBNET_2_NAME")
	SUBNET_2_TAG_STATUS=$?
	if [ $SUBNET_2_TAG_STATUS -eq 0 ]; then
		echo "Subnet 2 tag created with Name '$SUBNET_2_NAME' for VPC '$VPC_NAME'."
	else
		echo "Error while creating Subnet 2 tag"
		exit $SUBNET_2_TAG_STATUS
	fi

	#Create Subnet 3
	echo -e "\n\n-----------------------Creating 3rd Subnet-----------------------\n\n"
	SUBNET_3_ID=$(aws ec2 create-subnet \
		--vpc-id $VPC_ID \
		--cidr-block $SUBNET_3_CIDR \
		--availability-zone $SUBNET_3_AZ \
		--query 'Subnet.{SubnetId:SubnetId}' \
		--output text \
		--region $REGION)
	SUBNET_3_STATUS=$?
	if [ $SUBNET_3_STATUS -eq 0 ]; then
		echo "Subnet1 created with ID $SUBNET_3_ID for Vpc $VPC_NAME."
	else
		echo "Error while creating Subnet 3"
		exit $SUBNET_3_STATUS
	fi

	#Create Tag for Subnet 3
	echo -e "\n\n-----------------------Adding Tags to Subnet 3-----------------------\n\n"
	SUBNET_3_TAG=$(aws ec2 create-tags \
		--resources $SUBNET_3_ID \
		--tags "Key=Name,Value=$SUBNET_3_NAME")
	SUBNET_3_TAG_STATUS=$?
	if [ $SUBNET_3_TAG_STATUS -eq 0 ]; then
		echo "Subnet 3 tag created with Name '$SUBNET_3_NAME' for VPC '$VPC_NAME'."
	else
		echo "Error while creating Subnet 3 tag"
		exit $SUBNET_3_TAG_STATUS
	fi

	#Create Internet Gateway
	echo -e "\n\n-----------------------Creating Internet Gateway-----------------------\n\n"
	INTERNET_GATEWAY_ID=$(aws ec2 create-internet-gateway \
		--query 'InternetGateway.{InternetGatewayId:InternetGatewayId}' \
		--output text \
		--region $REGION)
	INTERNET_GATEWAY_STATUS=$?
	if [ $INTERNET_GATEWAY_STATUS -eq 0 ]; then
		echo "Internet Gateway created with Id '$INTERNET_GATEWAY_ID'."
	else
		echo "Error while creating Internet Gateway"
		exit $INTERNET_GATEWAY_STATUS
	fi

	#Create Tag for Internet Gateway
	echo -e "\n\n--------------------Adding Tags to Internet Gateway--------------------\n\n"
	INTERNET_GATEWAY_TAG=$(aws ec2 create-tags \
		--resources $INTERNET_GATEWAY_ID \
		--tags "Key=Name,Value=$INTERNET_GATEWAY_NAME")
	INTERNET_GATEWAY_TAG_STATUS=$?
	if [ $INTERNET_GATEWAY_TAG_STATUS -eq 0 ]; then
		echo "Internet Gateway tag created with Name '$INTERNET_GATEWAY_NAME' for Internet Gateway '$INTERNET_GATEWAY_ID'."
	else
		echo "Error while creating Internet Gateway Tag"
		exit $INTERNET_GATEWAY_TAG_STATUS
	fi

	#Attach Internet Gateway to VPC
	echo -e "\n\n--------------------Attaching Internet Gateway to VPC--------------------\n\n"
	INTERNET_GATEWAY_ATTACHED=$(aws ec2 attach-internet-gateway \
		--vpc-id $VPC_ID \
		--internet-gateway-id $INTERNET_GATEWAY_ID \
		--region $REGION)
	INTERNET_GATEWAY_ATTACHED_STATUS=$?
	if [ $INTERNET_GATEWAY_ATTACHED_STATUS -eq 0 ]; then
		echo "Internet Gateway '$INTERNET_GATEWAY_NAME' attached to VPC  '$VPC_NAME'."
	else
		echo "Error while attaching Internet gateway to VPC"
		exit $INTERNET_GATEWAY_ATTACHED_STATUS
	fi

	#Create Route Table
	echo -e "\n\n--------------------Creating Route Table--------------------\n\n"
	ROUTE_TABLE_ID=$(aws ec2 create-route-table \
		--vpc-id $VPC_ID \
		--query 'RouteTable.{RouteTableId:RouteTableId}' \
		--output text \
		--region $REGION)
	ROUTE_TABLE_STATUS=$?
	if [ $ROUTE_TABLE_STATUS -eq 0 ]; then
		echo "Route Table created with ID $ROUTE_TABLE_ID in region $REGION."
	else
		echo "Error while creating Route Table"
		exit $ROUTE_TABLE_STATUS
	fi

	#Create Tag for Route Table
	echo -e "\n\n-----------------------Adding Tags to Route Table-----------------------\n\n"
	ROUTE_TABLE_TAG=$(aws ec2 create-tags \
		--resources $ROUTE_TABLE_ID \
		--tags "Key=Name,Value=$ROUTE_TABLE_NAME")
	ROUTE_TABLE_TAG_STATUS=$?
	if [ $ROUTE_TABLE_TAG_STATUS -eq 0 ]; then
		echo "Route Table tag created with Name '$ROUTE_TABLE_NAME'."
	else
		echo "Error while creating Route Table tag"
		exit $ROUTE_TABLE_TAG_STATUS
	fi

	#Attach Subnet1 to Route Table
	echo -e "\n\n--------------------Attaching Subnet1 to Route Table--------------------\n\n"
	SUBNET_1_ASSOCIATION=$(aws ec2 associate-route-table \
		--route-table-id $ROUTE_TABLE_ID \
		--subnet-id $SUBNET_1_ID \
		--output text)
	SUBNET_1_ASSOCIATION_STATUS=$?
	if [ $SUBNET_1_ASSOCIATION_STATUS -eq 0 ]; then
		echo "Subnet 1 associated with Route Table with id: '$SUBNET_1_ASSOCIATION'."
	else
		echo "Error while attaching Subnet 1 to Route Table"
		exit $SUBNET_1_ASSOCIATION_STATUS
	fi

	#Attach Subnet2 to Route Table
	echo -e "\n\n--------------------Attaching Subnet2 to Route Table--------------------\n\n"
	SUBNET_2_ASSOCIATION=$(aws ec2 associate-route-table \
		--route-table-id $ROUTE_TABLE_ID \
		--subnet-id $SUBNET_2_ID \
		--output text)
	SUBNET_2_ASSOCIATION_STATUS=$?
	if [ $SUBNET_2_ASSOCIATION_STATUS -eq 0 ]; then
		echo "Subnet 2 associated with Route Table with id: '$SUBNET_2_ASSOCIATION'."
	else
		echo "Error while attaching Subnet 2 to Route Table"
		exit $SUBNET_2_ASSOCIATION_STATUS
	fi

	#Attach Subnet3 to Route Table
	echo -e "\n\n--------------------Attaching Subnet3 to Route Table--------------------\n\n"
	SUBNET_3_ASSOCIATION=$(aws ec2 associate-route-table \
		--route-table-id $ROUTE_TABLE_ID \
		--subnet-id $SUBNET_3_ID \
		--output text)
	SUBNET_3_ASSOCIATION_STATUS=$?
	if [ $SUBNET_3_ASSOCIATION_STATUS -eq 0 ]; then
		echo "Subnet 3 associated with Route Table with id: '$SUBNET_3_ASSOCIATION'."
	else
		echo "Error while attaching Subnet 3 to Route Table"
		exit $SUBNET_3_ASSOCIATION_STATUS
	fi

	#Create Public Route from Route Table to Internet Gateway
	echo -e "\n\n-------Creating Public Route from Route Table to Internet Gateway-------\n\n"
	PUBLIC_ROUTE=$(aws ec2 create-route \
		--route-table-id $ROUTE_TABLE_ID \
		--destination-cidr-block 0.0.0.0/0 \
		--gateway-id $INTERNET_GATEWAY_ID \
		--region $REGION)
	PUBLIC_ROUTE_STATUS=$?
	if [ $ROUTE_TABLE_TAG_STATUS -eq 0 ]; then
		echo "Public Route created with CIDR block  0.0.0.0/0 and Internet Gateway '$INTERNET_GATEWAY_NAME' to Route Table '$ROUTE_TABLE_NAME'."
	else
		echo "Error while creating Route Table tag"
		exit $ROUTE_TABLE_TAG_STATUS
	fi

	#Configuring Security Groups
	echo -e "\n\n-----------------------Modifying Security Groups-----------------------\n\n"
	SG_DEFAULT_RULE=$(aws ec2 describe-security-groups \
		--filters "Name=vpc-id,Values=$VPC_ID" \
		--query 'SecurityGroups[0].IpPermissions')
	SG_GROUP_ID=$(aws ec2 describe-security-groups \
		--filters "Name=vpc-id,Values=$VPC_ID" \
		--query 'SecurityGroups[0].{GroupId:GroupId}' \
		--output text)


	REMOVE_DEFAULT=$(aws ec2 revoke-security-group-ingress \
		--cli-input-json "{\"GroupId\": \"$SG_GROUP_ID\", \"IpPermissions\": $SG_DEFAULT_RULE}")
	REMOVE_DEFAULT_STATUS=$?
	if [ $REMOVE_DEFAULT_STATUS -eq 0 ]; then
		echo "Deleted default All Inbound Traffic rule"
	else
		echo "Error while deleting All Inbound Traffic rule"
		exit $REMOVE_DEFAULT_STATUS
	fi
	TCP22=$(aws ec2 authorize-security-group-ingress \
		--group-id $SG_GROUP_ID \
		--protocol tcp \
		--port 22 \
		--cidr 0.0.0.0\/0)
	TCP22_STATUS=$?
	if [ $TCP22_STATUS -eq 0 ]; then
		echo "Added TCP Inbound traffic rule on port 22"
	else
		echo "Error while adding Inbound Traffic rule for port 22"
		exit $TCP22_STATUS
	fi
	TCP80=$(aws ec2 authorize-security-group-ingress \
		--group-id $SG_GROUP_ID \
		--protocol tcp \
		--port 80 \
		--cidr 0.0.0.0\/0)
	TCP80_STATUS=$?
	if [ $TCP80_STATUS -eq 0 ]; then
		echo "Added TCP Inbound traffic rule on port 80"
	else
		echo "Error while adding Inbound Traffic rule for port 80"
		exit $TCP22_STATUS
	fi

	echo -e "\n\n---------------------------------------------------------------------------"
	echo -e "Successfully created Infrastructure as Code with AWS Command Line Interface"
	echo -e "---------------------------------------------------------------------------"



else

	echo "VPC name already exists, please provide another name!"
	exit 1
fi




