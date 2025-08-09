package com.leaderboard.model;

public class ScoreEntry {
    private String userId;
    private String gameName;
    private double score;
    private long timestampMs;

    public ScoreEntry(String userId, String gameName, double score, long timestampMs) {
        this.userId = userId;
        this.gameName = gameName;
        this.score = score;
        this.timestampMs = timestampMs;
    }

    public String getUserId() {
        return userId;
    }

    public String getGameName() {
        return gameName;
    }

    public double getScore() {
        return score;
    }

    public long getTimestampMs() {
        return timestampMs;
    }
}