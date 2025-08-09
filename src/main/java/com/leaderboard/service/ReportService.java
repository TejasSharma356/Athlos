package com.leaderboard.service;

import com.leaderboard.model.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportService {
    private final ScoreService scoreService;
    private final AuthService authService;

    public ReportService(ScoreService scoreService, AuthService authService) {
        this.scoreService = scoreService;
        this.authService = authService;
    }

    public LinkedHashMap<String, Double> generateTopPlayers(String gameName, long fromTs, long toTs, int limit) {
        Map<String, Double> top = scoreService.getTopPlayersInDateRange(gameName, fromTs, toTs, limit);
        LinkedHashMap<String, Double> ordered = new LinkedHashMap<>();
        top.entrySet().forEach(e -> ordered.put(resolveUsername(e.getKey()), e.getValue()));
        return ordered;
    }

    public File exportToCsv(String gameName, long fromTs, long toTs, int limit, File file) throws IOException {
        LinkedHashMap<String, Double> data = generateTopPlayers(gameName, fromTs, toTs, limit);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Game,From,To\n");
            writer.write(gameName + "," + formatTs(fromTs) + "," + formatTs(toTs) + "\n");
            writer.write("Rank,Username,Score\n");
            int i = 1;
            for (Map.Entry<String, Double> e : data.entrySet()) {
                writer.write(i++ + "," + e.getKey() + "," + e.getValue() + "\n");
            }
        }
        return file;
    }

    public File exportToTxt(String gameName, long fromTs, long toTs, int limit, File file) throws IOException {
        LinkedHashMap<String, Double> data = generateTopPlayers(gameName, fromTs, toTs, limit);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Top Players Report\n");
            writer.write("Game: " + gameName + "\n");
            writer.write("From: " + formatTs(fromTs) + " To: " + formatTs(toTs) + "\n\n");
            int i = 1;
            for (Map.Entry<String, Double> e : data.entrySet()) {
                writer.write(String.format("%d. %s - %.2f\n", i++, e.getKey(), e.getValue()));
            }
        }
        return file;
    }

    private String resolveUsername(String userId) {
        User u = authService.findById(userId);
        return u != null ? u.getUsername() : userId;
    }

    private String formatTs(long ts) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(Instant.ofEpochMilli(ts));
    }
}