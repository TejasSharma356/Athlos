package com.athlos.controller;

import com.athlos.dto.LeaderboardEntryDTO;
import com.athlos.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class LeaderboardController {
    
    @Autowired
    private LeaderboardService leaderboardService;
    
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
}

