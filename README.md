# Real-Time Leaderboard System

Java Swing desktop application that simulates a competitive ranking system for users across multiple games/activities. It uses Redis (Sorted Sets) via Jedis for real-time ranking.

## Features
- User authentication (register/login) with Admin and Regular roles
- Submit scores to selected game/activity
- Real-time leaderboard per game using Redis ZSETs
- View leaderboard with rank, username, score, game
- My Rank view and personal score history
- Admin dashboard for game management and top player reports (CSV/TXT export) within a date range
- Input validation and Redis connection error handling
- Sample data bootstrap for testing

## Technology Stack
- Java 11
- Swing for GUI
- Redis with Jedis client
- Maven + Shade plugin (fat JAR)

## Getting Started

### Prerequisites
- Java 11+
- Redis server running locally or accessible via network
- Maven 3.8+

### Configure Redis
By default, the app connects to `localhost:6379` without authentication.

You can override via environment variables:
- `REDIS_HOST` (default: `localhost`)
- `REDIS_PORT` (default: `6379`)
- `REDIS_PASSWORD` (optional)

### Build
```bash
mvn -q -DskipTests package
```
This will produce a fat jar at `target/real-time-leaderboard-system-1.0.0-shaded.jar`.

### Run
```bash
java -jar target/real-time-leaderboard-system-1.0.0-shaded.jar
```

## Default Credentials and Sample Data
On first startup, the app bootstraps:
- Admin: username `admin`, password `admin123`
- Users: `alice`/`password`, `bob`/`password`
- Games: `Chess`, `Tetris`, `SpeedType`
- Sample scores for the above users

## Mockups
See `mockups/mockups.md` for UI mockups.

## Export Samples
See `sample_exports/top_players_report_sample.csv` for a sample CSV export.

## Notes
- Passwords are hashed with SHA-256 for demo purposes. Do not use this in production.
- All Redis keys are namespaced under prefixes like `user:*`, `game:*`, `scores:*`.

## License
MIT
