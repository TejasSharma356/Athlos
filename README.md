# Athlos Fitness App

Athlos is a gamified fitness web app. Run/walk to trace paths, auto-claim territory on the map, and compete on real-time leaderboards.

##  Features

- **Real-time GPS Tracking**: Track your runs with live location updates
- **Territory Claiming**: Claim areas by running/walking paths
- **Leaderboards**: Compete with friends and other users
- **Social Features**: Connect with friends and share achievements
- **Real-time Updates**: Live leaderboard updates via WebSocket
- **Responsive Design**: Mobile-first design optimized for fitness use

##  Project Structure

```
athlos-fitness-app/
  App.tsx                  # Root React component (single-screen shell)
  index.html               # HTML shell (Tailwind CDN + Leaflet CDN)
  index.tsx                # React/Vite entry
  index.css                # Global styles and small utilities
  types.ts                 # Shared UI types
  vite.config.ts           # Vite + env definitions
  tsconfig.json            # TypeScript config

  src/services/
    api.ts                 # REST client (JWT-aware)
    websocket.ts           # SockJS+STOMP client

  screens/                 # Feature screens (Welcome, Auth, Home, Run, Profile, etc.)
  components/              # UI icons/components

  backend/                 # Spring Boot app
    src/main/java/com/athlos/
      config/              # Security, WebSocket, CORS
      controller/          # REST controllers
      dto/                 # API DTOs
      entity/              # JPA entities (PostGIS types)
      repository/          # Spring Data repositories
      service/             # Business logic (incl. BCrypt)
      util/                # JwtUtil
      filter/              # JwtAuthenticationFilter
    src/main/resources/
      application.yml      # Spring config
      data.sql             # Seed data (BCrypt passwords)
    pom.xml

  docs/                    # Developer docs (API, testing, implementation)
  docker-compose.yml       # Postgres + Backend + Frontend
  frontend/Dockerfile      # Frontend image
  backend/Dockerfile       # Backend image
  start.sh / start.bat     # Convenience scripts
```

##  Tech Stack

### Backend
- Spring Boot 3.2.0 (Java 17)
- PostgreSQL 15 + PostGIS (spatial data)
- Spring WebSocket (STOMP)
- JTS (Java Topology Suite)
- Spring Security + JWT + BCrypt

### Frontend
- React 19 + TypeScript
- Tailwind CSS (via CDN)
- Leaflet (via CDN)
- SockJS + STOMP

##  Prerequisites

- Java 17 or higher
- Node.js 18 or higher
- PostgreSQL 15 with PostGIS extension
- Docker and Docker Compose (optional)

## 🚀 Quick Start (Docker - Local Development)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd athlos-fitness-app
   ```

2. **Start the application**
   ```bash
   docker-compose up --build
   ```

3. **Access the application**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080/api
   - WebSocket: ws://localhost:8080/ws

## 🌐 Deploy to Railway (Production - FREE!)

**Deploy your app to the cloud in 5 minutes:**

1. **Push to GitHub**
   ```bash
   git add .
   git commit -m "Ready for Railway deployment"
   git push origin main
   ```

2. **Deploy to Railway**
   - Go to [railway.app](https://railway.app)
   - Sign up with GitHub
   - Click "New Project" → "Deploy from GitHub repo"
   - Select your repository
   - Railway auto-detects your Docker setup!

3. **Add PostgreSQL Database**
   - In Railway dashboard, click "+ New"
   - Select "Database" → "PostgreSQL"
   - Railway automatically enables PostGIS!

4. **Set Environment Variables**
   ```
   JWT_SECRET=your-super-secret-jwt-key
   JWT_EXPIRATION=86400000
   CORS_ALLOWED_ORIGINS=https://your-app.railway.app
   SPRING_PROFILES_ACTIVE=production
   ```

5. **You're Live!** 🎉
   - Your app: `https://your-app.railway.app`
   - **Free tier**: Perfect for development
   - **Production**: $5/month for reliable hosting

### Why Railway?
- ✅ **Zero configuration** - Just connect GitHub
- ✅ **Automatic HTTPS** - SSL certificates included
- ✅ **Built-in PostgreSQL** - With PostGIS support
- ✅ **Auto-scaling** - Handles traffic spikes
- ✅ **Global CDN** - Fast worldwide access
- ✅ **GitHub integration** - Push to deploy
- ✅ **Free tier** - Perfect for testing

##  Manual Setup

### Backend Setup

1. **Install PostgreSQL with PostGIS**
   ```bash
   # Ubuntu/Debian
   sudo apt-get install postgresql postgresql-contrib postgis
   
   # macOS with Homebrew
   brew install postgresql postgis
   ```

2. **Create database**
   ```sql
   CREATE DATABASE athlos_db;
   CREATE USER athlos_user WITH PASSWORD 'athlos_password';
   GRANT ALL PRIVILEGES ON DATABASE athlos_db TO athlos_user;
   \c athlos_db
   CREATE EXTENSION postgis;
   ```

3. **Start the backend**
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

4. **Start the frontend**
   ```bash
   npm install
   npm run dev
   ```

### Frontend Setup

1. **Install dependencies**
   ```bash
   npm install
   ```

2. **Start the development server**
   ```bash
   npm run dev
   ```

##  Try It

1. Sign up or sign in (sample user: john@example.com / password123)
2. **Set Goals**: Configure your daily step goals
3. **Start Running**: Begin a run to start tracking
4. **Claim Territory**: Your running path automatically claims territory
5. **Compete**: Check leaderboards and compete with friends

## 🔧 API Endpoints (summary)
 
### Auth
- POST `/api/users/register` — register
- POST `/api/auth/login` — login → returns `{ user, token }`
  - Send token in `Authorization: Bearer <token>` for protected endpoints

### Runs
- POST `/api/runs/start`
- POST `/api/runs/{id}/pause`
- POST `/api/runs/{id}/resume`
- POST `/api/runs/{id}/end`
- POST `/api/runs/{id}/point`
- GET  `/api/runs/user/{userId}`
- GET  `/api/runs/user/{userId}/active`

### Leaderboard
- GET `/api/leaderboard/daily`
- GET `/api/leaderboard/weekly`
- GET `/api/leaderboard/all-time`

##  WebSocket

- Subscriptions:
  - `/topic/leaderboard/daily`
  - `/topic/leaderboard/weekly`
  - `/topic/leaderboard/all-time`
- App destinations:
  - `/app/leaderboard/daily|weekly|all-time`

##  Database Schema

### Users
- Profiles, daily goal, last_active, current_location (Point, SRID 4326)

### Runs
- Start/end, duration, total_steps, distance_meters
- path (LineString), claimed_territory (Polygon)

### Territories
- polygon (Polygon), area_square_meters, is_active

##  Security

- Passwords hashed with BCrypt
- JWT login at `/api/auth/login`
- Protected routes require `Authorization: Bearer <token>`
- CORS: http://localhost:3000, http://localhost:5173

##  Deployment

### Environment Variables (prod)

```bash
# Backend
SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/athlos_db
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password
JWT_SECRET=change-me
JWT_EXPIRATION=86400000

# Frontend
REACT_APP_API_URL=https://your-api-domain.com/api
REACT_APP_WS_URL=wss://your-api-domain.com/ws
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request


##  Support

For support, please open an issue in the repository or contact the development team.

---

**Built with ❤️ by team Athlos**