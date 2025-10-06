package com.athlos.entity;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;

@Entity
@Table(name = "run_points")
public class RunPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", nullable = false)
    private Run run;
    
    @Column(name = "location", columnDefinition = "geometry(Point,4326)")
    private Point location;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "step_count")
    private Integer stepCount;
    
    @Column(name = "speed_mps")
    private Double speedMps;
    
    // Constructors
    public RunPoint() {}
    
    public RunPoint(Run run, Point location, LocalDateTime timestamp) {
        this.run = run;
        this.location = location;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Run getRun() { return run; }
    public void setRun(Run run) { this.run = run; }
    
    public Point getLocation() { return location; }
    public void setLocation(Point location) { this.location = location; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public Integer getStepCount() { return stepCount; }
    public void setStepCount(Integer stepCount) { this.stepCount = stepCount; }
    
    public Double getSpeedMps() { return speedMps; }
    public void setSpeedMps(Double speedMps) { this.speedMps = speedMps; }
}

