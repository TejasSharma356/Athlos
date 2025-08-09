package com.leaderboard.repository;

import com.leaderboard.config.RedisConfig;
import com.leaderboard.model.ScoreEntry;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.resps.Tuple;

import java.util.*;

public class ScoreRepository {
    private final JedisPool jedisPool;

    public ScoreRepository(RedisConfig config) {
        this.jedisPool = config.getJedisPool();
    }

    public void submitScore(String userId, String gameName, double score, long timestampMs) {
        try (Jedis jedis = jedisPool.getResource()) {
            String leaderboardKey = leaderboardKey(gameName);
            Double existing = jedis.zscore(leaderboardKey, userId);
            if (existing == null || score > existing) {
                jedis.zadd(leaderboardKey, score, userId);
            }
            String userHistoryKey = userHistoryKey(userId, gameName);
            jedis.lpush(userHistoryKey, timestampMs + "|" + score);
            // Global per-game history for reporting by date range
            String globalHistoryKey = globalHistoryKey(gameName);
            String member = userId + "|" + score + "|" + timestampMs;
            jedis.zadd(globalHistoryKey, timestampMs, member);
        }
    }

    public List<ScoreEntry> getTopScores(String gameName, int limit) {
        try (Jedis jedis = jedisPool.getResource()) {
            java.util.List<Tuple> tuples = jedis.zrevrangeWithScores(leaderboardKey(gameName), 0, limit - 1);
            List<ScoreEntry> list = new ArrayList<>();
            for (Tuple t : tuples) {
                list.add(new ScoreEntry(t.getElement(), gameName, t.getScore(), 0));
            }
            return list;
        }
    }

    public Long getRank(String userId, String gameName) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long rank = jedis.zrevrank(leaderboardKey(gameName), userId);
            if (rank == null) return null;
            return rank + 1; // 1-based rank
        }
    }

    public Double getBestScore(String userId, String gameName) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zscore(leaderboardKey(gameName), userId);
        }
    }

    public List<ScoreEntry> getUserHistory(String userId, String gameName, int limit) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> entries = jedis.lrange(userHistoryKey(userId, gameName), 0, limit - 1);
            List<ScoreEntry> list = new ArrayList<>();
            for (String e : entries) {
                String[] parts = e.split("\\|");
                long ts = Long.parseLong(parts[0]);
                double score = Double.parseDouble(parts[1]);
                list.add(new ScoreEntry(userId, gameName, score, ts));
            }
            return list;
        }
    }

    public Map<String, Double> getTopPlayersInDateRange(String gameName, long fromTs, long toTs, int limit) {
        try (Jedis jedis = jedisPool.getResource()) {
            java.util.List<String> entries = jedis.zrangeByScore(globalHistoryKey(gameName), fromTs, toTs);
            Map<String, Double> userToMax = new HashMap<>();
            for (String member : entries) {
                String[] parts = member.split("\\|");
                String userId = parts[0];
                double score = Double.parseDouble(parts[1]);
                userToMax.merge(userId, score, Math::max);
            }
            // Sort by score desc and cap to limit
            List<Map.Entry<String, Double>> sorted = new ArrayList<>(userToMax.entrySet());
            sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
            Map<String, Double> result = new LinkedHashMap<>();
            int count = 0;
            for (Map.Entry<String, Double> e : sorted) {
                result.put(e.getKey(), e.getValue());
                count++;
                if (count >= limit) break;
            }
            return result;
        }
    }

    private String leaderboardKey(String gameName) { return "scores:" + gameName; }
    private String userHistoryKey(String userId, String gameName) { return "history:" + userId + ":" + gameName; }
    private String globalHistoryKey(String gameName) { return "scores_history:" + gameName; }
}