package com.athlos.dto;

import java.time.LocalDateTime;

public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private Integer age;
    private String gender;
    private Integer dailyStepGoal;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private LocalDateTime lastActive;
    
    // Constructors
    public UserDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public Integer getDailyStepGoal() { return dailyStepGoal; }
    public void setDailyStepGoal(Integer dailyStepGoal) { this.dailyStepGoal = dailyStepGoal; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastActive() { return lastActive; }
    public void setLastActive(LocalDateTime lastActive) { this.lastActive = lastActive; }
}

