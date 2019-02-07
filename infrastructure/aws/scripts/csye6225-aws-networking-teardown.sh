#!/bin/bash

VPC_NAME=$1


if [ $# -ne 1 ]; then
	echo "--------------------------Error in Parameters--------------------------"
	echo -e "You need to provide following parameters in the same order as follows:\n\
Vpc Name"
exit 1
fi

#Fetching VPC_ID

	echo "--------------------------Fetching VPC ID--------------------------"
	VPC_ID=$(aws ec2 describe-vpcs \
        --filter "Name=tag:Name,Values=$VPC_NAME" \
        --query 'Vpcs[*].{id:VpcId}' \
        --output text)

VPC_ID_STATUS=$?

if [ -z $VPC_ID ]; then
	echo "VPC ID: $VPC_NAME doesn't exists."
	exit 1
fi


#Deleting Subnets

	echo "----------------------Fetching Subnets associated with VPC---------------------"
SUBNET_TABLE=$(aws ec2 describe-subnets --query "Subnets[?VpcId=='$VPC_ID'].SubnetId" --output text)

for i in $SUBNET_TABLE
do

echo "Found Subnet with ID $i"
aws ec2 delete-subnet --subnet-id $i
echo "Subnet $i deleted successfully!"

done

#Fetching ROUTE TABLE ID

	echo "--------------------------Fetching ROUTE TABLE ID--------------------------"

ROUTE_TABLE_IDS=$(aws ec2 describe-route-tables \
	--query "RouteTables[?VpcId=='$VPC_ID'].RouteTableId" \
	--output text)

ROUTE_TABLE_ID_STATUS=$?

if [ $ROUTE_TABLE_ID_STATUS -eq 0 ]; then
	echo "ROUTE TABLE fetched successfully."
else
	echo "Error while fetching ROUTE TABLES ID"
	exit $ROUTE_TABLE_ID_STATUS
fi


# Retrieving main route table
MAIN_ROUTE_TABLE_ID=$(aws ec2 describe-route-tables \
	--query "RouteTables[?(VpcId=='$VPC_ID' &&  Associations[?Main!=null])].RouteTableId" \
	--output text)

#Delete Route-Table
	echo "--------------------------Deleting ROUTE TABLE ID--------------------------"
for i in $ROUTE_TABLE_IDS
do

	if [[ $i != $MAIN_ROUTE_TABLE_ID ]]; then
		aws ec2 delete-route-table --route-table-id $i
	fi

done
echo "ROUTE TABLE deleted successfully"

#Fetching Internet Gateway ID

	echo "----------------------Fetching Internet Gateway ID----------------------"
INTERNET_GATEWAY_ID=$(aws ec2 describe-internet-gateways --query "InternetGateways[?Attachments[?VpcId=='$VPC_ID']].InternetGatewayId" --output text)

INTERNET_GATEWAY_ID_STATUS=$?

if [ $INTERNET_GATEWAY_ID_STATUS -eq 0 ]; then
	echo "INTERNET GATEWAY ID: $INTERNET_GATEWAY_ID fetched successfully."
else
	echo "Error while fetching INTERNET GATEWAY ID"
	exit $INTERNET_GATEWAY_ID_STATUS
fi


#Detaching Internet Gateway from VPC

	echo "--------------------Detaching Internet Gateway from VPC--------------------"

aws ec2 detach-internet-gateway --internet-gateway-id $INTERNET_GATEWAY_ID --vpc-id $VPC_ID

DETACH_STATUS=$?

if [ $DETACH_STATUS -eq 0 ]; then
	echo "INTERNET GATEWAY: $INTERNET_GATEWAY_ID detached from VPC successfully."
else
	echo "Error while detaching INTERNET GATEWAY from VPC"
	exit $DETACH_STATUS
fi

#Delete Internet Gateway

	echo "------------------------Deleting Internet Gateway--------------------------"

aws ec2 delete-internet-gateway --internet-gateway-id $INTERNET_GATEWAY_ID

DELETE_STATUS=$?

if [ $DELETE_STATUS -eq 0 ]; then
	echo "INTERNET GATEWAY: $INTERNET_GATEWAY_ID deleted successfully."
else
	echo "Error while deleting INTERNET GATEWAY"
	exit $DELETE_STATUS
fi

#Delete VPC

	echo "-------------------------------Deleting VPC-------------------------------"

aws ec2 delete-vpc --vpc-id $VPC_ID

VPC_DELETE_STATUS=$?

if [ $VPC_DELETE_STATUS -eq 0 ]; then
	echo "VPC: $VPC_ID deleted successfully."
else
	echo "Error while deleting VPC"
	exit $VPC_DELETE_STATUS
fi

echo -e "\n\n------------------------------------------------------------------------------"
echo -e "Successfully terminated Infrastructure as Code with AWS Command Line Interface"
echo -e "------------------------------------------------------------------------------"


