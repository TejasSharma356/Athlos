# ATHLOS: The Real-Life Paper.io

## Project Description
ATHLOS is a web application designed to gamify physical activity by transforming our university campus into a live, competitive game. Inspired by the popular game `Paper.io`, the system allows users to "claim" virtual land by running and enclosing an area on a map with their path. The core of the application is a real-time leaderboard that ranks users based on the total amount of territory they have captured. This project aims to encourage physical fitness and foster a fun, competitive community among students.

## Features
* **Real-Time Location Tracking:** Accurately displays a user's current position on the campus map using GPS.
* **Path Visualization:** Draws a dynamic line on the map to visualize the user's running path.
* **Area Claiming:** Detects when a user has completed a closed loop and calculates the area of the enclosed territory.
* **Live Leaderboard:** A real-time ranking of all users based on their claimed area.
* **Instant Notifications:** Notifies a user in real-time if their claimed territory is captured by another player.

## Tech Stack
* **Backend:** Java with **Spring Boot**
* **Frontend:** HTML, CSS, and JavaScript
* **Real-Time Communication:** **WebSockets**
* **Geospatial Data:** **PostgreSQL** with the **PostGIS** extension
* **Mapping API:** **Google Maps Platform**

## Installation and Setup

### Prerequisites
* Java Development Kit (JDK) 17 or higher
* Maven
* PostgreSQL installed with the PostGIS extension
* Node.js and npm (for the frontend)
* A Google Maps API Key

### Backend Setup
1.  Clone the repository:
    ```bash
    git clone [repository_url]
    cd athlos-backend
    ```
2.  Configure your database connection in `src/main/resources/application.properties`. Update the following properties with your PostgreSQL credentials:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/athlos_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```
3.  Configure your Google Maps API Key in the application properties.
4.  Build and run the Spring Boot application:
    ```bash
    ./mvnw spring-boot:run
    ```

### Frontend Setup
1.  Navigate to the frontend directory:
    ```bash
    cd ../athlos-frontend
    ```
2.  Install dependencies:
    ```bash
    npm install
    ```
3.  Start the development server:
    ```bash
    npm start
    ```

## Usage
1.  Open the web application in your browser at `http://localhost:3000`.
2.  Register and log in with a new user account.
3.  Click "Start Run" and begin moving around the defined map area. The application will track your path.
4.  To claim land, complete a closed loop by running back to your starting point or crossing your own path.
5.  View the real-time leaderboard to see how you rank against other players.

## Team
* Tejas Sharma 
* Vighnesh Singh Dhanai
* Satvik Sharma

---
