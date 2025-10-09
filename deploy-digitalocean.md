# 🌊 Deploy Athlos Fitness App to DigitalOcean App Platform

## Prerequisites
- GitHub account
- DigitalOcean account
- Credit card for billing

## Step-by-Step Deployment

### 1. Prepare Your Repository
```bash
# Make sure all changes are committed
git add .
git commit -m "Prepare for DigitalOcean deployment"
git push origin main
```

### 2. Create DigitalOcean App

1. **Go to [DigitalOcean App Platform](https://cloud.digitalocean.com/apps)**
2. **Click "Create App"**
3. **Connect GitHub** and select your repository
4. **DigitalOcean will auto-detect your Docker setup**

### 3. Configure Services

#### Backend Service
- **Source**: Your repository
- **Build Command**: `cd backend && mvn clean package`
- **Run Command**: `java -jar target/athlos-backend-0.0.1-SNAPSHOT.jar`
- **Port**: 8080

#### Frontend Service
- **Source**: Your repository
- **Build Command**: `npm install && npm run build`
- **Run Command**: `npm run preview`
- **Port**: 3000

### 4. Add Database

1. **Click "Create Database"**
2. **Select "PostgreSQL"**
3. **Choose region** (closest to your users)
4. **Enable PostGIS extension** in database settings

### 5. Configure Environment Variables

```bash
# Database (DigitalOcean will provide these)
SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-url
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key
JWT_EXPIRATION=86400000

# CORS
CORS_ALLOWED_ORIGINS=https://your-app.ondigitalocean.app
```

### 6. Deploy!

1. **Review configuration**
2. **Click "Create Resources"**
3. **Wait for deployment** (5-10 minutes)

### 7. Access Your App

Your app will be available at: `https://your-app.ondigitalocean.app`

## 🎉 You're Live!

## Cost Breakdown
- **App Platform**: $12/month (Basic plan)
- **Database**: $15/month (1GB RAM, 1 vCPU)
- **Total**: ~$27/month for production setup

## Benefits
- ✅ **Managed infrastructure**
- ✅ **Automatic HTTPS**
- ✅ **Built-in monitoring**
- ✅ **Easy scaling**
- ✅ **Global CDN**
