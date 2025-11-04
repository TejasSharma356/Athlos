package com.athlos.service;

import com.athlos.dto.PointDTO;
import com.athlos.dto.RunDTO;
import com.athlos.dto.LeaderboardEntryDTO;
import com.athlos.entity.Run;
import com.athlos.entity.RunPoint;
import com.athlos.entity.User;
import com.athlos.repository.RunRepository;
import com.athlos.repository.UserRepository;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RunService {
    
    @Autowired
    private RunRepository runRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private LeaderboardService leaderboardService;
    
    private final GeometryFactory geometryFactory = new GeometryFactory();
    
    public RunDTO startRun(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        
        // End any active runs first
        Optional<Run> activeRun = runRepository.findByUserAndIsActiveTrue(user);
        if (activeRun.isPresent()) {
            endRun(activeRun.get().getId());
        }
        
        Run run = new Run(user);
        run = runRepository.save(run);
        return convertToDTO(run);
    }
    
    public RunDTO pauseRun(Long runId) {
        Run run = runRepository.findById(runId).orElseThrow(() -> new RuntimeException("Run not found"));
        run.setIsActive(false);
        run = runRepository.save(run);
        return convertToDTO(run);
    }
    
    public RunDTO resumeRun(Long runId) {
        Run run = runRepository.findById(runId).orElseThrow(() -> new RuntimeException("Run not found"));
        run.setIsActive(true);
        run = runRepository.save(run);
        return convertToDTO(run);
    }
    
    public RunDTO endRun(Long runId) {
        Run run = runRepository.findById(runId).orElseThrow(() -> new RuntimeException("Run not found"));
        run.setEndTime(LocalDateTime.now());
        run.setIsActive(false);
        
        if (run.getStartTime() != null) {
            run.setDurationSeconds(java.time.Duration.between(run.getStartTime(), run.getEndTime()).getSeconds());
        }
        
        // Recalculate final distance and steps
        double totalDistance = calculateTotalDistance(run);
        run.setDistanceMeters(totalDistance);
        
        // Calculate steps from distance (0.78m per step)
        final double STEP_LENGTH_METERS = 0.78;
        int totalSteps = (int) Math.round(totalDistance / STEP_LENGTH_METERS);
        run.setTotalSteps(totalSteps);
        
        // Create claimed territory polygon from path
        if (run.getPath() != null && run.getPath().getNumPoints() > 2) {
            Polygon territory = createTerritoryFromPath(run.getPath());
            run.setClaimedTerritory(territory);
        }
        
        run = runRepository.save(run);
        
        // Broadcast leaderboard updates
        broadcastLeaderboardUpdates();
        
        return convertToDTO(run);
    }
    
    public RunDTO addRunPoint(Long runId, Double latitude, Double longitude, Integer stepCount) {
        Run run = runRepository.findById(runId).orElseThrow(() -> new RuntimeException("Run not found"));
        
        Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        RunPoint runPoint = new RunPoint(run, location, LocalDateTime.now());
        
        if (run.getRunPoints() == null) {
            run.setRunPoints(new ArrayList<>());
        }
        
        // Calculate distance from previous point
        double distanceIncrement = 0.0;
        if (!run.getRunPoints().isEmpty()) {
            RunPoint lastPoint = run.getRunPoints().get(run.getRunPoints().size() - 1);
            distanceIncrement = calculateDistance(
                lastPoint.getLocation().getY(), lastPoint.getLocation().getX(),
                latitude, longitude
            );
        }
        
        // Calculate steps from distance (0.78m per step)
        final double STEP_LENGTH_METERS = 0.78;
        int calculatedSteps = (int) Math.round(distanceIncrement / STEP_LENGTH_METERS);
        runPoint.setStepCount(calculatedSteps);
        
        run.getRunPoints().add(runPoint);
        
        // Update path
        updateRunPath(run);
        
        // Calculate total distance from all points
        double totalDistance = calculateTotalDistance(run);
        run.setDistanceMeters(totalDistance);
        
        // Calculate total steps from distance
        int totalSteps = (int) Math.round(totalDistance / STEP_LENGTH_METERS);
        run.setTotalSteps(totalSteps);
        
        run = runRepository.save(run);
        return convertToDTO(run);
    }
    
    public List<RunDTO> getUserRuns(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return runRepository.findByUserOrderByStartTimeDesc(user)
                .stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }
    
    public Optional<RunDTO> getActiveRun(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return runRepository.findByUserAndIsActiveTrue(user)
                .map(this::convertToDTO);
    }
    
    private void updateRunPath(Run run) {
        if (run.getRunPoints() != null && !run.getRunPoints().isEmpty()) {
            Coordinate[] coordinates = run.getRunPoints().stream()
                    .map(rp -> rp.getLocation().getCoordinate())
                    .toArray(Coordinate[]::new);
            
            LineString path = geometryFactory.createLineString(coordinates);
            run.setPath(path);
        }
    }
    
    /**
     * Calculate distance between two GPS coordinates using Haversine formula
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Earth radius in meters
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    
    /**
     * Calculate total distance from all run points
     */
    private double calculateTotalDistance(Run run) {
        if (run.getRunPoints() == null || run.getRunPoints().size() < 2) {
            return 0.0;
        }
        
        double totalDistance = 0.0;
        for (int i = 1; i < run.getRunPoints().size(); i++) {
            RunPoint prev = run.getRunPoints().get(i - 1);
            RunPoint curr = run.getRunPoints().get(i);
            totalDistance += calculateDistance(
                prev.getLocation().getY(), prev.getLocation().getX(),
                curr.getLocation().getY(), curr.getLocation().getX()
            );
        }
        return totalDistance;
    }
    
    private Polygon createTerritoryFromPath(LineString path) {
        // Create a simple polygon by connecting the path points
        // In a real implementation, you might want more sophisticated territory creation
        Coordinate[] coords = path.getCoordinates();
        if (coords.length < 3) {
            return null;
        }
        
        // Create a simple rectangular territory around the path
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
        
        for (Coordinate coord : coords) {
            minX = Math.min(minX, coord.x);
            maxX = Math.max(maxX, coord.x);
            minY = Math.min(minY, coord.y);
            maxY = Math.max(maxY, coord.y);
        }
        
        // Add some buffer around the path
        double buffer = 0.001; // Approximately 100m buffer
        Coordinate[] territoryCoords = {
            new Coordinate(minX - buffer, minY - buffer),
            new Coordinate(maxX + buffer, minY - buffer),
            new Coordinate(maxX + buffer, maxY + buffer),
            new Coordinate(minX - buffer, maxY + buffer),
            new Coordinate(minX - buffer, minY - buffer) // Close the polygon
        };
        
        LinearRing ring = geometryFactory.createLinearRing(territoryCoords);
        return geometryFactory.createPolygon(ring);
    }
    
    private RunDTO convertToDTO(Run run) {
        RunDTO dto = new RunDTO();
        dto.setId(run.getId());
        dto.setUserId(run.getUser().getId());
        dto.setStartTime(run.getStartTime());
        dto.setEndTime(run.getEndTime());
        dto.setDurationSeconds(run.getDurationSeconds());
        dto.setTotalSteps(run.getTotalSteps());
        dto.setDistanceMeters(run.getDistanceMeters());
        dto.setIsActive(run.getIsActive());
        
        // Convert path to DTO
        if (run.getPath() != null) {
            List<PointDTO> pathPoints = new ArrayList<>();
            for (int i = 0; i < run.getPath().getNumPoints(); i++) {
                Coordinate coord = run.getPath().getCoordinateN(i);
                pathPoints.add(new PointDTO(coord.y, coord.x)); // y=lat, x=lon
            }
            dto.setPath(pathPoints);
        }
        
        // Convert claimed territory to DTO
        if (run.getClaimedTerritory() != null) {
            List<PointDTO> territoryPoints = new ArrayList<>();
            Coordinate[] coords = run.getClaimedTerritory().getExteriorRing().getCoordinates();
            for (Coordinate coord : coords) {
                territoryPoints.add(new PointDTO(coord.y, coord.x)); // y=lat, x=lon
            }
            dto.setClaimedTerritory(territoryPoints);
        }
        
        return dto;
    }
    
    private void broadcastLeaderboardUpdates() {
        try {
            // Get fresh leaderboard data
            List<LeaderboardEntryDTO> dailyLeaderboard = leaderboardService.getDailyLeaderboard();
            List<LeaderboardEntryDTO> weeklyLeaderboard = leaderboardService.getWeeklyLeaderboard();
            List<LeaderboardEntryDTO> allTimeLeaderboard = leaderboardService.getAllTimeLeaderboard();
            
            // Broadcast to all connected clients
            messagingTemplate.convertAndSend("/topic/leaderboard/daily", dailyLeaderboard);
            messagingTemplate.convertAndSend("/topic/leaderboard/weekly", weeklyLeaderboard);
            messagingTemplate.convertAndSend("/topic/leaderboard/all-time", allTimeLeaderboard);
        } catch (Exception e) {
            System.err.println("Error broadcasting leaderboard updates: " + e.getMessage());
        }
    }
}

