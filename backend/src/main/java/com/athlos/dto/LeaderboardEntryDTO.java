package com.athlos.dto;

public class LeaderboardEntryDTO {
    private Long userId;
    private String name;
    private String avatar;
    private Integer totalSteps;
    private Integer rank;
    private Double totalDistance;
    private Integer territoriesClaimed;
    
    // Constructors
    public LeaderboardEntryDTO() {}
    
    public LeaderboardEntryDTO(Long userId, String name, String avatar, Integer totalSteps, Integer rank) {
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
        this.totalSteps = totalSteps;
        this.rank = rank;
    }
    
    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    
    public Integer getTotalSteps() { return totalSteps; }
    public void setTotalSteps(Integer totalSteps) { this.totalSteps = totalSteps; }
    
    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }
    
    public Double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(Double totalDistance) { this.totalDistance = totalDistance; }
    
    public Integer getTerritoriesClaimed() { return territoriesClaimed; }
    public void setTerritoriesClaimed(Integer territoriesClaimed) { this.territoriesClaimed = territoriesClaimed; }
}

