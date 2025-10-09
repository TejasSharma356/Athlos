package com.athlos.controller;

import com.athlos.dto.LeaderboardEntryDTO;
import com.athlos.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class LeaderboardController {
    
    @Autowired
    private LeaderboardService leaderboardService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @GetMapping("/daily")
    public ResponseEntity<List<LeaderboardEntryDTO>> getDailyLeaderboard() {
        List<LeaderboardEntryDTO> leaderboard = leaderboardService.getDailyLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }
    
    @GetMapping("/weekly")
    public ResponseEntity<List<LeaderboardEntryDTO>> getWeeklyLeaderboard() {
        List<LeaderboardEntryDTO> leaderboard = leaderboardService.getWeeklyLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }
    
    @GetMapping("/all-time")
    public ResponseEntity<List<LeaderboardEntryDTO>> getAllTimeLeaderboard() {
        List<LeaderboardEntryDTO> leaderboard = leaderboardService.getAllTimeLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshLeaderboards() {
        try {
            // Get fresh leaderboard data
            List<LeaderboardEntryDTO> dailyLeaderboard = leaderboardService.getDailyLeaderboard();
            List<LeaderboardEntryDTO> weeklyLeaderboard = leaderboardService.getWeeklyLeaderboard();
            List<LeaderboardEntryDTO> allTimeLeaderboard = leaderboardService.getAllTimeLeaderboard();
            
            // Broadcast to all connected clients
            messagingTemplate.convertAndSend("/topic/leaderboard/daily", dailyLeaderboard);
            messagingTemplate.convertAndSend("/topic/leaderboard/weekly", weeklyLeaderboard);
            messagingTemplate.convertAndSend("/topic/leaderboard/all-time", allTimeLeaderboard);
            
            return ResponseEntity.ok("Leaderboards refreshed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error refreshing leaderboards: " + e.getMessage());
        }
    }
}

