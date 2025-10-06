package com.athlos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RunDTO {
    private Long id;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationSeconds;
    private Integer totalSteps;
    private Double distanceMeters;
    private List<PointDTO> path;
    private List<PointDTO> claimedTerritory;
    private Boolean isActive;
    
    // Constructors
    public RunDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public Long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Long durationSeconds) { this.durationSeconds = durationSeconds; }
    
    public Integer getTotalSteps() { return totalSteps; }
    public void setTotalSteps(Integer totalSteps) { this.totalSteps = totalSteps; }
    
    public Double getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(Double distanceMeters) { this.distanceMeters = distanceMeters; }
    
    public List<PointDTO> getPath() { return path; }
    public void setPath(List<PointDTO> path) { this.path = path; }
    
    public List<PointDTO> getClaimedTerritory() { return claimedTerritory; }
    public void setClaimedTerritory(List<PointDTO> claimedTerritory) { this.claimedTerritory = claimedTerritory; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}

