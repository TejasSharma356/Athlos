# 🚀 Deploy Athlos Fitness App to Railway

## Prerequisites
- GitHub account
- Railway account (free at [railway.app](https://railway.app))

## Step-by-Step Deployment

### 1. Prepare Your Repository
```bash
# Make sure all changes are committed
git add .
git commit -m "Prepare for deployment"
git push origin main
```

### 2. Deploy to Railway

1. **Go to [Railway.app](https://railway.app)** and sign up
2. **Click "New Project"** → **"Deploy from GitHub repo"**
3. **Select your repository**
4. **Railway will auto-detect your Docker setup**

### 3. Configure Environment Variables

In Railway dashboard, go to your project → Variables tab and add:

```bash
# Database (Railway will provide these automatically)
SPRING_DATASOURCE_URL=jdbc:postgresql://your-railway-db-url
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production
JWT_EXPIRATION=86400000

# CORS (Railway will provide your domain)
CORS_ALLOWED_ORIGINS=https://your-app.railway.app
```

### 4. Add PostgreSQL Database

1. In Railway dashboard, click **"+ New"**
2. Select **"Database"** → **"PostgreSQL"**
3. Railway will automatically:
   - Create the database
   - Set environment variables
   - Enable PostGIS extension

### 5. Deploy!

Railway will automatically:
- Build your Docker containers
- Deploy your app
- Provide HTTPS URL
- Handle scaling

### 6. Access Your App

Your app will be available at: `https://your-app.railway.app`

## 🎉 You're Live!

Your Athlos fitness app is now deployed and accessible worldwide!

## Monitoring & Management

- **Logs**: View in Railway dashboard
- **Metrics**: Built-in monitoring
- **Scaling**: Automatic based on traffic
- **Updates**: Push to GitHub = auto-deploy

## Cost
- **Free tier**: Perfect for development/testing
- **Production**: $5/month for reliable hosting
