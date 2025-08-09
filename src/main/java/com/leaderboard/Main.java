package com.leaderboard;

import com.leaderboard.config.RedisConfig;
import com.leaderboard.service.AuthService;
import com.leaderboard.service.GameService;
import com.leaderboard.service.ScoreService;
import com.leaderboard.ui.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize Redis connection early to fail fast if misconfigured
        RedisConfig redisConfig = RedisConfig.getInstance();

        // Bootstrap sample data
        AuthService authService = new AuthService(redisConfig);
        GameService gameService = new GameService(redisConfig);
        ScoreService scoreService = new ScoreService(redisConfig, authService, gameService);
        try {
            Bootstrapper.bootstrapSampleData(authService, gameService, scoreService);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Launch UI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(authService, gameService, scoreService);
            frame.setVisible(true);
        });
    }
}