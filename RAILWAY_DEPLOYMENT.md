# 🚀 Deploy Athlos Fitness App to Railway

## Prerequisites
- GitHub account
- Railway account (free at [railway.app](https://railway.app))

## Step-by-Step Deployment

### 1. Prepare Your Code
```bash
# Make sure all changes are committed
git add .
git commit -m "Ready for Railway deployment"
git push origin main
```

### 2. Deploy to Railway

1. **Go to [Railway.app](https://railway.app)** and sign up
2. **Click "New Project"** → **"Deploy from GitHub repo"**
3. **Select your repository**
4. **Railway will auto-detect your Docker setup**

### 3. Add PostgreSQL Database

1. In Railway dashboard, click **"+ New"**
2. Select **"Database"** → **"PostgreSQL"**
3. Railway will automatically:
   - Create the database
   - Set environment variables
   - Enable PostGIS extension

### 4. Configure Environment Variables

In Railway dashboard, go to your project → Variables tab and add:

```bash
# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production
JWT_EXPIRATION=86400000

# CORS (Railway will provide your domain)
CORS_ALLOWED_ORIGINS=https://your-app.railway.app

# Spring Profile
SPRING_PROFILES_ACTIVE=production
```

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

## Troubleshooting

### Common Issues:

1. **Build fails**: Check Docker logs in Railway dashboard
2. **Database connection**: Verify environment variables are set
3. **CORS errors**: Update CORS_ALLOWED_ORIGINS with your Railway domain
4. **WebSocket not working**: Ensure your domain supports WebSocket connections

### Getting Help:
- Railway docs: [docs.railway.app](https://docs.railway.app)
- Railway Discord: [discord.gg/railway](https://discord.gg/railway)
