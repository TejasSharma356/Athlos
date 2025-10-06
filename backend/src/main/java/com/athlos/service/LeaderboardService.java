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
        
        return userRepository.findAll().stream()
                .map(user -> {
                    Integer totalSteps = runRepository.getTotalStepsByUserAndDateRange(user, startOfDay, endOfDay);
                    Double totalDistance = runRepository.getTotalDistanceByUserAndDateRange(user, startOfDay, endOfDay);
                    Integer territoriesClaimed = territoryRepository.countActiveTerritoriesByUser(user);
                    
                    LeaderboardEntryDTO entry = new LeaderboardEntryDTO();
                    entry.setUserId(user.getId());
                    entry.setName(user.getName());
                    entry.setAvatar("https://i.pravatar.cc/40?u=" + user.getId());
                    entry.setTotalSteps(totalSteps != null ? totalSteps : 0);
                    entry.setTotalDistance(totalDistance != null ? totalDistance : 0.0);
                    entry.setTerritoriesClaimed(territoriesClaimed != null ? territoriesClaimed : 0);
                    
                    return entry;
                })
                .filter(entry -> entry.getTotalSteps() > 0)
                .sorted((a, b) -> Integer.compare(b.getTotalSteps(), a.getTotalSteps()))
                .peek(entry -> entry.setRank(getRank(entry, getDailyLeaderboard())))
                .limit(50)
                .collect(Collectors.toList());
    }
    
    public List<LeaderboardEntryDTO> getWeeklyLeaderboard() {
        LocalDateTime startOfWeek = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(7);
        LocalDateTime endOfWeek = LocalDateTime.now();
        
        return userRepository.findAll().stream()
                .map(user -> {
                    Integer totalSteps = runRepository.getTotalStepsByUserAndDateRange(user, startOfWeek, endOfWeek);
                    Double totalDistance = runRepository.getTotalDistanceByUserAndDateRange(user, startOfWeek, endOfWeek);
                    Integer territoriesClaimed = territoryRepository.countActiveTerritoriesByUser(user);
                    
                    LeaderboardEntryDTO entry = new LeaderboardEntryDTO();
                    entry.setUserId(user.getId());
                    entry.setName(user.getName());
                    entry.setAvatar("https://i.pravatar.cc/40?u=" + user.getId());
                    entry.setTotalSteps(totalSteps != null ? totalSteps : 0);
                    entry.setTotalDistance(totalDistance != null ? totalDistance : 0.0);
                    entry.setTerritoriesClaimed(territoriesClaimed != null ? territoriesClaimed : 0);
                    
                    return entry;
                })
                .filter(entry -> entry.getTotalSteps() > 0)
                .sorted((a, b) -> Integer.compare(b.getTotalSteps(), a.getTotalSteps()))
                .peek(entry -> entry.setRank(getRank(entry, getWeeklyLeaderboard())))
                .limit(50)
                .collect(Collectors.toList());
    }
    
    public List<LeaderboardEntryDTO> getAllTimeLeaderboard() {
        return userRepository.findAll().stream()
                .map(user -> {
                    Integer totalSteps = runRepository.getTotalStepsByUserAndDateRange(user, LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.now());
                    Double totalDistance = runRepository.getTotalDistanceByUserAndDateRange(user, LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.now());
                    Integer territoriesClaimed = territoryRepository.countActiveTerritoriesByUser(user);
                    
                    LeaderboardEntryDTO entry = new LeaderboardEntryDTO();
                    entry.setUserId(user.getId());
                    entry.setName(user.getName());
                    entry.setAvatar("https://i.pravatar.cc/40?u=" + user.getId());
                    entry.setTotalSteps(totalSteps != null ? totalSteps : 0);
                    entry.setTotalDistance(totalDistance != null ? totalDistance : 0.0);
                    entry.setTerritoriesClaimed(territoriesClaimed != null ? territoriesClaimed : 0);
                    
                    return entry;
                })
                .filter(entry -> entry.getTotalSteps() > 0)
                .sorted((a, b) -> Integer.compare(b.getTotalSteps(), a.getTotalSteps()))
                .peek(entry -> entry.setRank(getRank(entry, getAllTimeLeaderboard())))
                .limit(50)
                .collect(Collectors.toList());
    }
    
    private int getRank(LeaderboardEntryDTO entry, List<LeaderboardEntryDTO> leaderboard) {
        return leaderboard.indexOf(entry) + 1;
    }
}

