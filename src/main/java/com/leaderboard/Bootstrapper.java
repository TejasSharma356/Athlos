package com.leaderboard;

import com.leaderboard.model.Game;
import com.leaderboard.model.User;
import com.leaderboard.service.AuthService;
import com.leaderboard.service.GameService;
import com.leaderboard.service.ScoreService;

import java.time.Instant;
import java.util.Arrays;

public class Bootstrapper {

    public static void bootstrapSampleData(AuthService authService, GameService gameService, ScoreService scoreService) {
        // Games
        Arrays.asList(
                new Game(null, "Chess", "Classic strategy board game"),
                new Game(null, "Tetris", "Tile-matching puzzle game"),
                new Game(null, "SpeedType", "Typing speed challenge")
        ).forEach(gameService::createGameIfAbsent);

        // Users
        createUserIfAbsent(authService, "admin", "admin@example.com", "admin123", "ADMIN");
        createUserIfAbsent(authService, "alice", "alice@example.com", "password", "USER");
        createUserIfAbsent(authService, "bob", "bob@example.com", "password", "USER");

        // Sample scores
        User alice = authService.findByUsername("alice");
        User bob = authService.findByUsername("bob");
        if (alice != null) {
            scoreService.submitScore(alice.getId(), "Chess", 1200, Instant.now().minusSeconds(86400).toEpochMilli());
            scoreService.submitScore(alice.getId(), "Tetris", 8000, Instant.now().toEpochMilli());
            scoreService.submitScore(alice.getId(), "SpeedType", 85, Instant.now().minusSeconds(3600).toEpochMilli());
        }
        if (bob != null) {
            scoreService.submitScore(bob.getId(), "Chess", 1350, Instant.now().toEpochMilli());
            scoreService.submitScore(bob.getId(), "Tetris", 6000, Instant.now().minusSeconds(7200).toEpochMilli());
            scoreService.submitScore(bob.getId(), "SpeedType", 92, Instant.now().toEpochMilli());
        }
    }

    private static void createUserIfAbsent(AuthService authService, String username, String email, String password, String role) {
        if (authService.findByUsername(username) == null) {
            authService.register(username, email, password, role);
        }
    }
}