# Athlos Fitness App - Testing Guide

## 🧪 Testing the Complete Application

This guide will help you test all the features of the Athlos Fitness App MVP.

## 🚀 Quick Start

1. **Start the application:**
   ```bash
   # Windows
   start.bat
   
   # Linux/Mac
   ./start.sh
   ```

2. **Wait for services to start** (about 30 seconds)

3. **Open the app:** http://localhost:3000

## 👤 User Registration & Authentication

### Test User Registration
1. Click "Get Started" on the welcome screen
2. Fill in the registration form:
   - Name: Your Name
   - Email: your-email@example.com
   - Password: password123
3. Click "Sign Up"
4. Verify you're redirected to the personal info screen

### Test User Login
1. Click "Sign In" on the welcome screen
2. Use one of the sample accounts:
   - Email: john@example.com
   - Password: password123
3. Click "Sign In"
4. Verify you're redirected to the home screen
  - Inspect browser DevTools → Application → Local Storage
  - Ensure `athlos_token` and `athlos_user` are set

## 🏃‍♂️ Run Tracking

### Test Starting a Run
1. From the home screen, click "Start RUN"
2. Accept location permissions when prompted
3. Verify the run screen loads with:
   - Map showing your current location
   - Timer starting at 00:00:00
   - Pause/Resume button available

### Test Pausing/Resuming
1. Click the pause button (yellow circle)
2. Verify the timer stops
3. Click the resume button (green circle)
4. Verify the timer continues

### Test Location Tracking
1. Move around while the run is active
2. Verify:
   - Your location marker moves on the map
   - A red line traces your path
   - Location data is sent to the backend

### Test Ending a Run
1. Click "End Run"
2. Verify:
   - The run is saved to the backend
   - You're redirected to the home screen
   - A territory polygon is drawn on the map

## 🏆 Leaderboard Testing

### Test Real-time Updates
1. Open the app in two browser windows/tabs
2. Start a run in one window
3. In the other window, check the leaderboard
4. Verify the leaderboard updates in real-time

### Test Different Time Periods
1. Navigate to the leaderboard screen
2. Test switching between:
   - Daily leaderboard
   - Weekly leaderboard
   - All-time leaderboard

## 👥 Social Features

### Test User Search
1. From the home screen, scroll to "Connect with Friends"
2. Type in the search box
3. Verify search results appear
4. Test the "Add" button (currently mock functionality)

### Test Profile Management
1. Click on your avatar in the top right
2. Navigate to "Profile"
3. Test editing your profile information
4. Verify changes are saved

## 🗺️ Map Features

### Test GPS Accuracy
1. Start a run outdoors
2. Walk/run a specific path
3. End the run
4. Verify the drawn path matches your actual movement

### Test Territory Claiming
1. Complete a run with a closed loop path
2. Verify a territory polygon is created
3. Check that the territory area is calculated

## 🔧 Backend API Testing

### Test with Postman/curl

#### Register a new user:
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123","name":"Test User"}'
```

#### Get daily leaderboard:
```bash
curl http://localhost:8080/api/leaderboard/daily
```

#### Start a run (requires token):
```bash
# Login and capture token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"password123"}' | jq -r .token)

curl -X POST http://localhost:8080/api/runs/start \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"userId":1}'
```

## 🌐 WebSocket Testing

### Test Real-time Communication
1. Open browser developer tools
2. Go to the Console tab
3. Look for WebSocket connection messages
4. Start/end runs and verify WebSocket events

### Test Leaderboard Updates
1. Open two browser windows
2. In one window, start a run
3. In the other window, watch the leaderboard
4. Verify real-time updates

## 🐛 Common Issues & Solutions

### Location Not Working
- **Issue:** "Location Required" error
- **Solution:** 
  - Ensure location permissions are granted
  - Try refreshing the page
  - Check if you're using HTTPS (required for location in some browsers)

### Backend Connection Failed
- **Issue:** API calls failing
- **Solution:**
  - Check if backend is running: http://localhost:8080/api/users
  - Restart Docker containers: `docker-compose restart`
  - Check Docker logs: `docker-compose logs backend`

### Frontend Not Loading
- **Issue:** White screen or errors
- **Solution:**
  - Check if frontend is running: http://localhost:3000
  - Restart frontend: `docker-compose restart frontend`
  - Check browser console for errors

### Database Connection Issues
- **Issue:** Database errors in backend logs
- **Solution:**
  - Check if PostgreSQL is running: `docker-compose ps`
  - Restart database: `docker-compose restart postgres`
  - Check database logs: `docker-compose logs postgres`

## 📊 Performance Testing

### Test with Multiple Users
1. Open multiple browser windows
2. Register different users
3. Start runs simultaneously
4. Verify the system handles multiple concurrent users

### Test Large Datasets
1. Create multiple runs with many GPS points
2. Test leaderboard performance with many users
3. Verify map rendering with complex paths

## 🔒 Security Testing

### Test Input Validation
1. Try registering with invalid email formats
2. Test with very long names/passwords
3. Verify proper error handling

### Test CORS
1. Try making API calls from different domains
2. Verify CORS headers are properly set

## 📱 Mobile Testing

### Test on Mobile Devices
1. Open the app on a mobile device
2. Test GPS accuracy while walking/running
3. Verify touch interactions work properly
4. Test the responsive design

## 🎯 Feature Checklist

- [ ] User registration works
- [ ] User login works
- [ ] Run tracking starts correctly
- [ ] GPS location is accurate
- [ ] Pause/resume functionality works
- [ ] Run ending saves data correctly
- [ ] Territory claiming works
- [ ] Leaderboard displays correctly
- [ ] Real-time updates work
- [ ] User search works
- [ ] Profile editing works
- [ ] Map rendering is smooth
- [ ] WebSocket connection is stable
- [ ] API endpoints respond correctly
- [ ] Error handling is proper
- [ ] Mobile experience is good

## 📈 Monitoring

### Check Application Health
- Backend health: http://localhost:8080/api/users
- Frontend health: http://localhost:3000
- Database health: Check Docker logs

### Monitor Performance
- Check browser developer tools for performance metrics
- Monitor Docker container resource usage
- Check database query performance

## 🚨 Troubleshooting

If you encounter issues:

1. **Check Docker status:** `docker-compose ps`
2. **View logs:** `docker-compose logs [service-name]`
3. **Restart services:** `docker-compose restart`
4. **Rebuild containers:** `docker-compose up --build`
5. **Check system resources:** Ensure Docker has enough memory/CPU

## 📝 Reporting Issues

When reporting issues, include:
- Operating system and browser version
- Steps to reproduce the issue
- Expected vs actual behavior
- Screenshots or error messages
- Browser console logs (if applicable)

