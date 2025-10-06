#!/bin/bash

echo "🚀 Starting Athlos Fitness App..."

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

# Check if Docker Compose is available
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose and try again."
    exit 1
fi

echo "📦 Building and starting containers..."
docker-compose up --build -d

echo "⏳ Waiting for services to start..."
sleep 30

echo "🔍 Checking service health..."

# Check if backend is running
if curl -f http://localhost:8080/api/users > /dev/null 2>&1; then
    echo "✅ Backend is running at http://localhost:8080"
else
    echo "⚠️  Backend might still be starting up..."
fi

# Check if frontend is running
if curl -f http://localhost:3000 > /dev/null 2>&1; then
    echo "✅ Frontend is running at http://localhost:3000"
else
    echo "⚠️  Frontend might still be starting up..."
fi

echo ""
echo "🎉 Athlos Fitness App is starting up!"
echo "📱 Frontend: http://localhost:3000"
echo "🔧 Backend API: http://localhost:8080/api"
echo "🌐 WebSocket: ws://localhost:8080/ws"
echo ""
echo "📋 Sample users for testing:"
echo "   - john@example.com / password123"
echo "   - jane@example.com / password123"
echo "   - mike@example.com / password123"
echo ""
echo "To stop the application, run: docker-compose down"
