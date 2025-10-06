package com.athlos.entity;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Polygon;
import java.time.LocalDateTime;

@Entity
@Table(name = "territories")
public class Territory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id")
    private Run run;
    
    @Column(name = "polygon", columnDefinition = "geometry(Polygon,4326)")
    private Polygon polygon;
    
    @Column(name = "area_square_meters")
    private Double areaSquareMeters;
    
    @Column(name = "claimed_at")
    private LocalDateTime claimedAt = LocalDateTime.now();
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    // Constructors
    public Territory() {}
    
    public Territory(User user, Run run, Polygon polygon) {
        this.user = user;
        this.run = run;
        this.polygon = polygon;
        this.areaSquareMeters = calculateArea(polygon);
    }
    
    private Double calculateArea(Polygon polygon) {
        // Convert from degrees to meters (approximate)
        // This is a simplified calculation - in production, use proper geodetic calculations
        return polygon.getArea() * 111000 * 111000; // Rough conversion
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Run getRun() { return run; }
    public void setRun(Run run) { this.run = run; }
    
    public Polygon getPolygon() { return polygon; }
    public void setPolygon(Polygon polygon) { this.polygon = polygon; }
    
    public Double getAreaSquareMeters() { return areaSquareMeters; }
    public void setAreaSquareMeters(Double areaSquareMeters) { this.areaSquareMeters = areaSquareMeters; }
    
    public LocalDateTime getClaimedAt() { return claimedAt; }
    public void setClaimedAt(LocalDateTime claimedAt) { this.claimedAt = claimedAt; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}

