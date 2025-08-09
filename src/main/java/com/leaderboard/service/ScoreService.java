package com.leaderboard.service;

import com.leaderboard.config.RedisConfig;
import com.leaderboard.model.Game;
import com.leaderboard.model.ScoreEntry;
import com.leaderboard.model.User;
import com.leaderboard.repository.ScoreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final AuthService authService;
    private final GameService gameService;

    public ScoreService(RedisConfig config, AuthService authService, GameService gameService) {
        this.scoreRepository = new ScoreRepository(config);
        this.authService = authService;
        this.gameService = gameService;
    }

    public void submitScore(String userId, String gameName, double score, long tsMs) {
        validateUserAndGame(userId, gameName);
        scoreRepository.submitScore(userId, gameName, score, tsMs);
    }

    public List<ScoreRow> getLeaderboard(String gameName, int limit) {
        List<ScoreEntry> entries = scoreRepository.getTopScores(gameName, limit);
        List<ScoreRow> rows = new ArrayList<>();
        long rank = 1;
        for (ScoreEntry e : entries) {
            User u = authService.findById(e.getUserId());
            String username = u != null ? u.getUsername() : e.getUserId();
            rows.add(new ScoreRow(rank++, username, e.getScore(), gameName));
        }
        return rows;
    }

    public Long getUserRank(String userId, String gameName) {
        return scoreRepository.getRank(userId, gameName);
    }

    public Double getUserBestScore(String userId, String gameName) { return scoreRepository.getBestScore(userId, gameName); }

    public List<ScoreEntry> getUserHistory(String userId, String gameName, int limit) {
        validateUserAndGame(userId, gameName);
        return scoreRepository.getUserHistory(userId, gameName, limit);
    }

    public Map<String, Double> getTopPlayersInDateRange(String gameName, long fromTs, long toTs, int limit) {
        return scoreRepository.getTopPlayersInDateRange(gameName, fromTs, toTs, limit);
    }

    private void validateUserAndGame(String userId, String gameName) {
        if (authService.findById(userId) == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        Game g = gameService.findByName(gameName);
        if (g == null) {
            throw new IllegalArgumentException("Invalid game: " + gameName);
        }
    }

    public static class ScoreRow {
        public final long rank;
        public final String username;
        public final double score;
        public final String gameName;

        public ScoreRow(long rank, String username, double score, String gameName) {
            this.rank = rank;
            this.username = username;
            this.score = score;
            this.gameName = gameName;
        }
    }
}