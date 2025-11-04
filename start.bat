@echo off
echo 🚀 Starting Athlos Fitness App...

REM Check if Docker is running
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker is not running. Please start Docker and try again.
    pause
    exit /b 1
)

REM Check if Docker Compose is available
docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker Compose is not installed. Please install Docker Compose and try again.
    pause
    exit /b 1
)

echo 📦 Building and starting containers...
docker-compose up --build -d    

echo ⏳ Waiting for services to start...
timeout /t 30 /nobreak >nul

echo 🔍 Checking service health...

REM Check if backend is running
curl -f http://localhost:8080/api/users >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Backend is running at http://localhost:8080
) else (
    echo ⚠️  Backend might still be starting up...
)

REM Check if frontend is running
curl -f http://localhost:3000 >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Frontend is running at http://localhost:3000
) else (
    echo ⚠️  Frontend might still be starting up...
)

echo.
echo 🎉 Athlos Fitness App is starting up!
echo 📱 Frontend: http://localhost:3000
echo 🔧 Backend API: http://localhost:8080/api
echo 🌐 WebSocket: ws://localhost:8080/ws
echo.
echo 📋 Sample users for testing:
echo    - john@example.com / password123
echo    - jane@example.com / password123
echo    - mike@example.com / password123
echo.
echo To stop the application, run: docker-compose down
pause
