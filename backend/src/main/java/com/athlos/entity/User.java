package com.athlos.entity;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String name;
    
    private Integer age;
    private String gender;
    private Integer dailyStepGoal = 6000;
    
    @Column(name = "current_location", columnDefinition = "geometry(Point,4326)")
    private Point currentLocation;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "last_active")
    private LocalDateTime lastActive = LocalDateTime.now();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Run> runs;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Territory> territories;
    
    // Constructors
    public User() {}
    
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public Integer getDailyStepGoal() { return dailyStepGoal; }
    public void setDailyStepGoal(Integer dailyStepGoal) { this.dailyStepGoal = dailyStepGoal; }
    
    public Point getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Point currentLocation) { this.currentLocation = currentLocation; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastActive() { return lastActive; }
    public void setLastActive(LocalDateTime lastActive) { this.lastActive = lastActive; }
    
    public List<Run> getRuns() { return runs; }
    public void setRuns(List<Run> runs) { this.runs = runs; }
    
    public List<Territory> getTerritories() { return territories; }
    public void setTerritories(List<Territory> territories) { this.territories = territories; }
}

