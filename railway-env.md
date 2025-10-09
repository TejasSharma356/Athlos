# Railway Environment Variables

Set these in your Railway dashboard:

## Database (Auto-provided by Railway)
- `DATABASE_URL` - Railway provides this automatically
- `PGUSER` - Railway provides this automatically  
- `PGPASSWORD` - Railway provides this automatically

## Application Variables
- `JWT_SECRET` - Set to a secure random string
- `JWT_EXPIRATION` - Set to `86400000` (24 hours)
- `CORS_ALLOWED_ORIGINS` - Set to your Railway domain
- `SPRING_PROFILES_ACTIVE` - Set to `production`
