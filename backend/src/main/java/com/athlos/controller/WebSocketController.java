package com.athlos.controller;

import com.athlos.dto.LeaderboardEntryDTO;
import com.athlos.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {
    
    @Autowired
    private LeaderboardService leaderboardService;
    
    @MessageMapping("/leaderboard/daily")
    @SendTo("/topic/leaderboard/daily")
    public List<LeaderboardEntryDTO> getDailyLeaderboard() {
        return leaderboardService.getDailyLeaderboard();
    }
    
    @MessageMapping("/leaderboard/weekly")
    @SendTo("/topic/leaderboard/weekly")
    public List<LeaderboardEntryDTO> getWeeklyLeaderboard() {
        return leaderboardService.getWeeklyLeaderboard();
    }
    
    @MessageMapping("/leaderboard/all-time")
    @SendTo("/topic/leaderboard/all-time")
    public List<LeaderboardEntryDTO> getAllTimeLeaderboard() {
        return leaderboardService.getAllTimeLeaderboard();
    }
}