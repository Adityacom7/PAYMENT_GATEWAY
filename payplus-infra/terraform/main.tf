# ~/payplus-infra/terraform/main.tf
terraform {
  required_version = ">= 1.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "ap-south-1"
}

# Create a simple VPC
resource "aws_vpc" "payplus_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true

  tags = {
    Name = "payplus-vpc"
  }
}

# Create TWO public subnets in DIFFERENT AZs
resource "aws_subnet" "public_subnet_1" {
  vpc_id                  = aws_vpc.payplus_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "ap-south-1a"
  map_public_ip_on_launch = true

  tags = {
    Name = "payplus-public-subnet-1a"
  }
}

resource "aws_subnet" "public_subnet_2" {
  vpc_id                  = aws_vpc.payplus_vpc.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "ap-south-1b"  # Different AZ!
  map_public_ip_on_launch = true

  tags = {
    Name = "payplus-public-subnet-1b"
  }
}

# Create internet gateway
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.payplus_vpc.id

  tags = {
    Name = "payplus-igw"
  }
}

# Create route table for public subnets
resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.payplus_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "payplus-public-rt"
  }
}

# Associate route table with both public subnets
resource "aws_route_table_association" "public_rta_1" {
  subnet_id      = aws_subnet.public_subnet_1.id
  route_table_id = aws_route_table.public_rt.id
}

resource "aws_route_table_association" "public_rta_2" {
  subnet_id      = aws_subnet.public_subnet_2.id
  route_table_id = aws_route_table.public_rt.id
}

# Create EKS cluster (now with TWO subnets)
resource "aws_eks_cluster" "payplus_cluster" {
  name     = "payplus-cluster"
  role_arn = aws_iam_role.cluster_role.arn

  vpc_config {
    subnet_ids = [aws_subnet.public_subnet_1.id, aws_subnet.public_subnet_2.id] # Both subnets!
  }

  depends_on = [
    aws_iam_role_policy_attachment.cluster_AmazonEKSClusterPolicy
  ]
}

# Create IAM role for EKS cluster
resource "aws_iam_role" "cluster_role" {
  name = "payplus-cluster-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "eks.amazonaws.com"
        }
      }
    ]
  })
}

# Attach policy to cluster role
resource "aws_iam_role_policy_attachment" "cluster_AmazonEKSClusterPolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
  role       = aws_iam_role.cluster_role.name
}

# Create managed node group (now with TWO subnets)
resource "aws_eks_node_group" "payplus_nodes" {
  cluster_name    = aws_eks_cluster.payplus_cluster.name
  node_group_name = "payplus-node-group"
  node_role_arn   = aws_iam_role.node_role.arn
  subnet_ids      = [aws_subnet.public_subnet_1.id, aws_subnet.public_subnet_2.id] # Both subnets!

  scaling_config {
    desired_size = 2  # This will create 1 node in each AZ!
    max_size     = 3
    min_size     = 1
  }

  instance_types = ["t3.medium"]

  depends_on = [
    aws_iam_role_policy_attachment.node_AmazonEKSWorkerNodePolicy,
    aws_iam_role_policy_attachment.node_AmazonEKS_CNI_Policy,
    aws_iam_role_policy_attachment.node_AmazonEC2ContainerRegistryReadOnly
  ]
}

# Create IAM role for nodes
resource "aws_iam_role" "node_role" {
  name = "payplus-node-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })
}

# Attach policies to node role
resource "aws_iam_role_policy_attachment" "node_AmazonEKSWorkerNodePolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
  role       = aws_iam_role.node_role.name
}

resource "aws_iam_role_policy_attachment" "node_AmazonEKS_CNI_Policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
  role       = aws_iam_role.node_role.name
}

resource "aws_iam_role_policy_attachment" "node_AmazonEC2ContainerRegistryReadOnly" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
  role       = aws_iam_role.node_role.name
}

# Output important information
output "cluster_name" {
  value = aws_eks_cluster.payplus_cluster.name
}

output "cluster_endpoint" {
  value = aws_eks_cluster.payplus_cluster.endpoint
}

output "cluster_security_group_id" {
  value = aws_eks_cluster.payplus_cluster.vpc_config[0].cluster_security_group_id
}