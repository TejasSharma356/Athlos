# Athlos Fitness App - API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
The API uses JWT-based authentication. Login returns `{ user, token }`. Send the token on subsequent requests via `Authorization: Bearer <token>`.

## Endpoints

### User Management

#### Register User
```http
POST /users/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe"
}
```

**Response:**
```json
{
  "id": 1,
  "email": "user@example.com",
  "name": "John Doe",
  "age": null,
  "gender": null,
  "dailyStepGoal": 6000,
  "latitude": null,
  "longitude": null,
  "createdAt": "2024-01-01T00:00:00",
  "lastActive": "2024-01-01T00:00:00"
}
```

#### Login User
```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "user": {
    "id": 1,
    "email": "user@example.com",
    "name": "John Doe",
    "dailyStepGoal": 6000,
    "createdAt": "2024-01-01T00:00:00",
    "lastActive": "2024-01-01T00:00:00"
  },
  "token": "<jwt-token>"
}
```

#### Get User
```http
GET /users/{id}
```

Send header:
```
Authorization: Bearer <token>
```

#### Update User
```http
PUT /users/{id}
Content-Type: application/json

{
  "name": "Updated Name",
  "age": 25,
  "gender": "Male",
  "dailyStepGoal": 8000
}
```

Headers:
```
Authorization: Bearer <token>
```

#### Update User Location
```http
PUT /users/{id}/location
Content-Type: application/json

{
  "latitude": 40.7128,
  "longitude": -74.0060
}
```

Headers:
```
Authorization: Bearer <token>
```

#### Search Users
```http
GET /users/search?q=search_term
```

### Run Management

#### Start Run
```http
POST /runs/start
Content-Type: application/json

{
  "userId": 1
}
```

Headers:
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "id": 1,
  "userId": 1,
  "startTime": "2024-01-01T10:00:00",
  "endTime": null,
  "durationSeconds": null,
  "totalSteps": 0,
  "distanceMeters": null,
  "path": [],
  "claimedTerritory": [],
  "isActive": true
}
```

#### Pause Run
```http
POST /runs/{runId}/pause
```

Headers:
```
Authorization: Bearer <token>
```

#### Resume Run
```http
POST /runs/{runId}/resume
```

Headers:
```
Authorization: Bearer <token>
```

#### End Run
```http
POST /runs/{runId}/end
```

Headers:
```
Authorization: Bearer <token>
```

#### Add Run Point
```http
POST /runs/{runId}/point
Content-Type: application/json

{
  "latitude": 40.7128,
  "longitude": -74.0060,
  "stepCount": 100
}
```

Headers:
```
Authorization: Bearer <token>
```

#### Get User Runs
```http
GET /runs/user/{userId}
```

Headers:
```
Authorization: Bearer <token>
```

#### Get Active Run
```http
GET /runs/user/{userId}/active
```

Headers:
```
Authorization: Bearer <token>
```

### Leaderboard

#### Get Daily Leaderboard
```http
GET /leaderboard/daily
```

**Response:**
```json
[
  {
    "userId": 1,
    "name": "John Doe",
    "avatar": "https://i.pravatar.cc/40?u=1",
    "totalSteps": 8500,
    "rank": 1,
    "totalDistance": 6.2,
    "territoriesClaimed": 3
  }
]
```

#### Get Weekly Leaderboard
```http
GET /leaderboard/weekly
```

#### Get All-Time Leaderboard
```http
GET /leaderboard/all-time
```

## WebSocket Events

### Connection
```javascript
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);
```

### Subscribe to Leaderboard Updates
```javascript
// Daily leaderboard
stompClient.subscribe('/topic/leaderboard/daily', (message) => {
  const leaderboard = JSON.parse(message.body);
  // Update UI with new leaderboard data
});

// Weekly leaderboard
stompClient.subscribe('/topic/leaderboard/weekly', (message) => {
  const leaderboard = JSON.parse(message.body);
  // Update UI with new leaderboard data
});

// All-time leaderboard
stompClient.subscribe('/topic/leaderboard/all-time', (message) => {
  const leaderboard = JSON.parse(message.body);
  // Update UI with new leaderboard data
});
```

### Request Leaderboard Updates
```javascript
// Request daily leaderboard update
stompClient.send('/app/leaderboard/daily', {}, '');

// Request weekly leaderboard update
stompClient.send('/app/leaderboard/weekly', {}, '');

// Request all-time leaderboard update
stompClient.send('/app/leaderboard/all-time', {}, '');
```

## Error Responses

### 400 Bad Request
```json
{
  "error": "Bad Request",
  "message": "Invalid request data"
}
```

### 401 Unauthorized
```json
{
  "error": "Unauthorized",
  "message": "Invalid credentials"
}
```

### 404 Not Found
```json
{
  "error": "Not Found",
  "message": "Resource not found"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

## Data Models

### User
```typescript
interface User {
  id: number;
  email: string;
  name: string;
  age?: number;
  gender?: string;
  dailyStepGoal: number;
  latitude?: number;
  longitude?: number;
  createdAt: string;
  lastActive: string;
}
```

### Run
```typescript
interface Run {
  id: number;
  userId: number;
  startTime: string;
  endTime?: string;
  durationSeconds?: number;
  totalSteps?: number;
  distanceMeters?: number;
  path?: Point[];
  claimedTerritory?: Point[];
  isActive: boolean;
}
```

### Point
```typescript
interface Point {
  latitude: number;
  longitude: number;
}
```

### LeaderboardEntry
```typescript
interface LeaderboardEntry {
  userId: number;
  name: string;
  avatar: string;
  totalSteps: number;
  rank: number;
  totalDistance?: number;
  territoriesClaimed?: number;
}
```

## Rate Limiting
Currently no rate limiting is implemented. In production, consider implementing rate limiting for API endpoints.

## CORS
CORS is configured to allow requests from:
- http://localhost:3000
- http://localhost:5173

## Database
The application uses PostgreSQL with PostGIS extension for geospatial data storage and queries.

