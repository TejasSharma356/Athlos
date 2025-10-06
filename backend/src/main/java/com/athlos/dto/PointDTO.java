package com.athlos.dto;

public class PointDTO {
    private Double latitude;
    private Double longitude;
    
    // Constructors
    public PointDTO() {}
    
    public PointDTO(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Getters and Setters
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}

