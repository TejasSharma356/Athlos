package com.athlos.entity;

import jakarta.persistence.*;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "runs")
public class Run {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "duration_seconds")
    private Long durationSeconds;
    
    @Column(name = "total_steps")
    private Integer totalSteps;
    
    @Column(name = "distance_meters")
    private Double distanceMeters;
    
    @Column(name = "path", columnDefinition = "geometry(LineString,4326)")
    private LineString path;
    
    @Column(name = "claimed_territory", columnDefinition = "geometry(Polygon,4326)")
    private Polygon claimedTerritory;
    
    @Column(name = "is_active")
    private Boolean isActive = false;
    
    @OneToMany(mappedBy = "run", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RunPoint> runPoints;
    
    // Constructors
    public Run() {}
    
    public Run(User user) {
        this.user = user;
        this.startTime = LocalDateTime.now();
        this.isActive = true;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
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
    
    public LineString getPath() { return path; }
    public void setPath(LineString path) { this.path = path; }
    
    public Polygon getClaimedTerritory() { return claimedTerritory; }
    public void setClaimedTerritory(Polygon claimedTerritory) { this.claimedTerritory = claimedTerritory; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public List<RunPoint> getRunPoints() { return runPoints; }
    public void setRunPoints(List<RunPoint> runPoints) { this.runPoints = runPoints; }
}

