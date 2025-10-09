package com.athlos.service;

import com.athlos.dto.LeaderboardEntryDTO;
import com.athlos.entity.User;
import com.athlos.repository.RunRepository;
import com.athlos.repository.TerritoryRepository;
import com.athlos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RunRepository runRepository;
    
    @Autowired
    private TerritoryRepository territoryRepository;
    
    public List<LeaderboardEntryDTO> getDailyLeaderboard() {
        LocalDateTime startOfDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        List<LeaderboardEntryDTO> entries = userRepository.findAll().stream()
                .map(user -> {
                    Long totalRunTime = runRepository.getTotalRunTimeByUserAndDateRange(user, startOfDay, endOfDay);
                    Double totalDistance = runRepository.getTotalDistanceByUserAndDateRange(user, startOfDay, endOfDay);
                    Integer totalSteps = runRepository.getTotalStepsByUserAndDateRange(user, startOfDay, endOfDay);
                    Integer territoriesClaimed = territoryRepository.countActiveTerritoriesByUser(user);
                    
                    LeaderboardEntryDTO entry = new LeaderboardEntryDTO();
                    entry.setUserId(user.getId());
                    entry.setName(user.getName());
                    entry.setAvatar("https://ui-avatars.com/api/?name=" + user.getName() + "&background=ef4444&color=ffffff&size=40");
                    entry.setTotalSteps(totalSteps != null ? totalSteps : 0);
                    entry.setTotalDistance(totalDistance != null ? totalDistance : 0.0);
                    entry.setTerritoriesClaimed(territoriesClaimed != null ? territoriesClaimed : 0);
                    
                    return entry;
                })
                .filter(entry -> entry.getTotalSteps() > 0 || entry.getTotalDistance() > 0)
                .sorted((a, b) -> {
                    // Primary sort by total steps, secondary by distance
                    int stepComparison = Integer.compare(b.getTotalSteps(), a.getTotalSteps());
                    if (stepComparison != 0) return stepComparison;
                    return Double.compare(b.getTotalDistance(), a.getTotalDistance());
                })
                .limit(50)
                .collect(Collectors.toList());
        
        // Set ranks after sorting
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setRank(i + 1);
        }
        
        return entries;
    }
    
    public List<LeaderboardEntryDTO> getWeeklyLeaderboard() {
        LocalDateTime startOfWeek = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(7);
        LocalDateTime endOfWeek = LocalDateTime.now();
        
        List<LeaderboardEntryDTO> entries = userRepository.findAll().stream()
                .map(user -> {
                    Long totalRunTime = runRepository.getTotalRunTimeByUserAndDateRange(user, startOfWeek, endOfWeek);
                    Double totalDistance = runRepository.getTotalDistanceByUserAndDateRange(user, startOfWeek, endOfWeek);
                    Integer totalSteps = runRepository.getTotalStepsByUserAndDateRange(user, startOfWeek, endOfWeek);
                    Integer territoriesClaimed = territoryRepository.countActiveTerritoriesByUser(user);
                    
                    LeaderboardEntryDTO entry = new LeaderboardEntryDTO();
                    entry.setUserId(user.getId());
                    entry.setName(user.getName());
                    entry.setAvatar("https://ui-avatars.com/api/?name=" + user.getName() + "&background=ef4444&color=ffffff&size=40");
                    entry.setTotalSteps(totalSteps != null ? totalSteps : 0);
                    entry.setTotalDistance(totalDistance != null ? totalDistance : 0.0);
                    entry.setTerritoriesClaimed(territoriesClaimed != null ? territoriesClaimed : 0);
                    
                    return entry;
                })
                .filter(entry -> entry.getTotalSteps() > 0 || entry.getTotalDistance() > 0)
                .sorted((a, b) -> {
                    // Primary sort by total steps, secondary by distance
                    int stepComparison = Integer.compare(b.getTotalSteps(), a.getTotalSteps());
                    if (stepComparison != 0) return stepComparison;
                    return Double.compare(b.getTotalDistance(), a.getTotalDistance());
                })
                .limit(50)
                .collect(Collectors.toList());
        
        // Set ranks after sorting
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setRank(i + 1);
        }
        
        return entries;
    }
    
    public List<LeaderboardEntryDTO> getAllTimeLeaderboard() {
        List<LeaderboardEntryDTO> entries = userRepository.findAll().stream()
                .map(user -> {
                    Long totalRunTime = runRepository.getTotalRunTimeByUserAndDateRange(user, LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.now());
                    Double totalDistance = runRepository.getTotalDistanceByUserAndDateRange(user, LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.now());
                    Integer totalSteps = runRepository.getTotalStepsByUserAndDateRange(user, LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.now());
                    Integer territoriesClaimed = territoryRepository.countActiveTerritoriesByUser(user);
                    
                    LeaderboardEntryDTO entry = new LeaderboardEntryDTO();
                    entry.setUserId(user.getId());
                    entry.setName(user.getName());
                    entry.setAvatar("https://ui-avatars.com/api/?name=" + user.getName() + "&background=ef4444&color=ffffff&size=40");
                    entry.setTotalSteps(totalSteps != null ? totalSteps : 0);
                    entry.setTotalDistance(totalDistance != null ? totalDistance : 0.0);
                    entry.setTerritoriesClaimed(territoriesClaimed != null ? territoriesClaimed : 0);
                    
                    return entry;
                })
                .filter(entry -> entry.getTotalSteps() > 0 || entry.getTotalDistance() > 0)
                .sorted((a, b) -> {
                    // Primary sort by total steps, secondary by distance
                    int stepComparison = Integer.compare(b.getTotalSteps(), a.getTotalSteps());
                    if (stepComparison != 0) return stepComparison;
                    return Double.compare(b.getTotalDistance(), a.getTotalDistance());
                })
                .limit(50)
                .collect(Collectors.toList());
        
        // Set ranks after sorting
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setRank(i + 1);
        }
        
        return entries;
    }
    
}

