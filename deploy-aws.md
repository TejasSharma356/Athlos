# ☁️ Deploy Athlos Fitness App to AWS

## Prerequisites
- AWS account
- AWS CLI installed
- Docker installed locally

## Architecture Overview
- **ECS** for container orchestration
- **RDS PostgreSQL** with PostGIS
- **Application Load Balancer** for routing
- **CloudFront** for global CDN
- **Route 53** for domain management

## Step-by-Step Deployment

### 1. Prepare Infrastructure

#### Create RDS Database
```bash
# Create RDS PostgreSQL instance with PostGIS
aws rds create-db-instance \
  --db-instance-identifier athlos-db \
  --db-instance-class db.t3.micro \
  --engine postgres \
  --master-username athlos \
  --master-user-password YourSecurePassword123 \
  --allocated-storage 20 \
  --vpc-security-group-ids sg-your-security-group
```

#### Create ECS Cluster
```bash
# Create ECS cluster
aws ecs create-cluster --cluster-name athlos-cluster
```

### 2. Build and Push Docker Images

```bash
# Build backend image
docker build -f backend/Dockerfile -t athlos-backend .

# Build frontend image  
docker build -f frontend/Dockerfile -t athlos-frontend .

# Tag for ECR
docker tag athlos-backend:latest your-account.dkr.ecr.region.amazonaws.com/athlos-backend:latest
docker tag athlos-frontend:latest your-account.dkr.ecr.region.amazonaws.com/athlos-frontend:latest

# Push to ECR
aws ecr get-login-password --region your-region | docker login --username AWS --password-stdin your-account.dkr.ecr.region.amazonaws.com
docker push your-account.dkr.ecr.region.amazonaws.com/athlos-backend:latest
docker push your-account.dkr.ecr.region.amazonaws.com/athlos-frontend:latest
```

### 3. Create ECS Task Definitions

#### Backend Task Definition
```json
{
  "family": "athlos-backend",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "256",
  "memory": "512",
  "executionRoleArn": "arn:aws:iam::account:role/ecsTaskExecutionRole",
  "containerDefinitions": [
    {
      "name": "athlos-backend",
      "image": "your-account.dkr.ecr.region.amazonaws.com/athlos-backend:latest",
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "SPRING_DATASOURCE_URL",
          "value": "jdbc:postgresql://your-rds-endpoint:5432/athlos_db"
        },
        {
          "name": "SPRING_DATASOURCE_USERNAME",
          "value": "athlos"
        },
        {
          "name": "SPRING_DATASOURCE_PASSWORD",
          "value": "YourSecurePassword123"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/athlos-backend",
          "awslogs-region": "your-region",
          "awslogs-stream-prefix": "ecs"
        }
      }
    }
  ]
}
```

### 4. Create ECS Services

```bash
# Create backend service
aws ecs create-service \
  --cluster athlos-cluster \
  --service-name athlos-backend-service \
  --task-definition athlos-backend:1 \
  --desired-count 1 \
  --launch-type FARGATE \
  --network-configuration "awsvpcConfiguration={subnets=[subnet-12345],securityGroups=[sg-12345],assignPublicIp=ENABLED}"
```

### 5. Configure Application Load Balancer

```bash
# Create ALB
aws elbv2 create-load-balancer \
  --name athlos-alb \
  --subnets subnet-12345 subnet-67890 \
  --security-groups sg-12345

# Create target groups
aws elbv2 create-target-group \
  --name athlos-backend-tg \
  --protocol HTTP \
  --port 8080 \
  --vpc-id vpc-12345 \
  --target-type ip
```

### 6. Set Up CloudFront Distribution

```bash
# Create CloudFront distribution
aws cloudfront create-distribution \
  --distribution-config file://cloudfront-config.json
```

## 🎉 You're Live!

Your app will be available at your CloudFront domain.

## Cost Estimation
- **ECS Fargate**: ~$15/month
- **RDS PostgreSQL**: ~$25/month  
- **Application Load Balancer**: ~$18/month
- **CloudFront**: ~$5/month
- **Total**: ~$63/month

## Benefits
- ✅ **Highly scalable**
- ✅ **Global performance**
- ✅ **Enterprise-grade security**
- ✅ **Full control**
- ✅ **AWS ecosystem integration**
