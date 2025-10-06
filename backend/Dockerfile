# ---------- Build stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn -B -q -e -DskipTests dependency:go-offline

# Build
COPY src ./src
RUN mvn -B -q -DskipTests clean package

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/athlos-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

