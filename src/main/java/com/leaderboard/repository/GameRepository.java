package com.leaderboard.repository;

import com.leaderboard.config.RedisConfig;
import com.leaderboard.model.Game;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameRepository {
    private final JedisPool jedisPool;

    public GameRepository(RedisConfig config) {
        this.jedisPool = config.getJedisPool();
    }

    public Game findByName(String name) {
        try (Jedis jedis = jedisPool.getResource()) {
            String id = jedis.get(gameNameKey(name));
            if (id == null) return null;
            Map<String, String> map = jedis.hgetAll(gameKey(id));
            if (map == null || map.isEmpty()) return null;
            return fromMap(map);
        }
    }

    public Game create(String name, String description) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.get(gameNameKey(name)) != null) {
                throw new IllegalStateException("Game already exists");
            }
            long nextId = jedis.incr("game:id:seq");
            String id = String.valueOf(nextId);
            Map<String, String> data = new HashMap<>();
            data.put("id", id);
            data.put("name", name);
            data.put("description", description);
            jedis.hset(gameKey(id), data);
            jedis.set(gameNameKey(name), id);
            jedis.sadd("games:all", name);
            return new Game(id, name, description);
        }
    }

    public List<Game> findAll() {
        try (Jedis jedis = jedisPool.getResource()) {
            List<Game> games = new ArrayList<>();
            for (String name : jedis.smembers("games:all")) {
                Game g = findByName(name);
                if (g != null) games.add(g);
            }
            return games;
        }
    }

    public void deleteByName(String name) {
        try (Jedis jedis = jedisPool.getResource()) {
            String id = jedis.get(gameNameKey(name));
            if (id != null) {
                jedis.del(gameKey(id));
                jedis.del(gameNameKey(name));
                jedis.srem("games:all", name);
                jedis.del("scores:" + name);
                jedis.del("scores_history:" + name);
            }
        }
    }

    private String gameKey(String id) { return "game:" + id; }
    private String gameNameKey(String name) { return "game:name:" + name.toLowerCase(); }

    private Game fromMap(Map<String, String> map) {
        return new Game(map.get("id"), map.get("name"), map.get("description"));
    }
}