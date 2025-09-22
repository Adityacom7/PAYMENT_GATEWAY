output "cluster_name" {
  description = "Kubernetes Cluster Name"
  value       = aws_eks_cluster.payplus_cluster.name
}

output "cluster_endpoint" {
  description = "Endpoint for EKS control plane"
  value       = aws_eks_cluster.payplus_cluster.endpoint
}

output "cluster_security_group_id" {
  description = "Security group ids attached to the cluster control plane"
  value       = aws_eks_cluster.payplus_cluster.vpc_config[0].cluster_security_group_id
}

output "vpc_id" {
  description = "ID of the VPC"
  value       = aws_vpc.payplus_vpc.id
}

output "public_subnet_ids" {
  description = "IDs of the public subnets"
  value       = aws_subnet.public_subnets[*].id
}

output "node_group_arn" {
  description = "ARN of the EKS node group"
  value       = aws_eks_node_group.payplus_nodes.arn
}

output "cluster_status" {
  description = "Status of the EKS cluster"
  value       = aws_eks_cluster.payplus_cluster.status
}