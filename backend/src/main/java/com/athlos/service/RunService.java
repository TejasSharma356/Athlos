package com.athlos.service;

import com.athlos.dto.PointDTO;
import com.athlos.dto.RunDTO;
import com.athlos.entity.Run;
import com.athlos.entity.RunPoint;
import com.athlos.entity.User;
import com.athlos.repository.RunRepository;
import com.athlos.repository.UserRepository;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
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
        
        // Create claimed territory polygon from path
        if (run.getPath() != null && run.getPath().getNumPoints() > 2) {
            Polygon territory = createTerritoryFromPath(run.getPath());
            run.setClaimedTerritory(territory);
        }
        
        run = runRepository.save(run);
        return convertToDTO(run);
    }
    
    public RunDTO addRunPoint(Long runId, Double latitude, Double longitude, Integer stepCount) {
        Run run = runRepository.findById(runId).orElseThrow(() -> new RuntimeException("Run not found"));
        
        Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        RunPoint runPoint = new RunPoint(run, location, LocalDateTime.now());
        runPoint.setStepCount(stepCount);
        
        if (run.getRunPoints() == null) {
            run.setRunPoints(new ArrayList<>());
        }
        run.getRunPoints().add(runPoint);
        
        // Update path
        updateRunPath(run);
        
        // Update total steps
        if (run.getTotalSteps() == null) {
            run.setTotalSteps(0);
        }
        run.setTotalSteps(run.getTotalSteps() + (stepCount != null ? stepCount : 0));
        
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
}

