# Multi-stage build for production
FROM maven:3.9.6-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY backend/pom.xml .
COPY backend/src ./src
RUN mvn -B -q -DskipTests clean package

FROM node:18-alpine AS frontend-build
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY . .
RUN npm run build

# Production stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy backend
COPY --from=backend-build /app/target/athlos-backend-0.0.1-SNAPSHOT.jar app.jar

# Copy frontend build
COPY --from=frontend-build /app/dist ./static

# Install nginx for serving frontend
RUN apk add --no-cache nginx
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 8080

# Start both backend and nginx
CMD ["sh", "-c", "java -jar app.jar & nginx -g 'daemon off;'"]
