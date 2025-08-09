package com.leaderboard.repository;

import com.leaderboard.config.RedisConfig;
import com.leaderboard.model.User;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final JedisPool jedisPool;

    public UserRepository(RedisConfig config) {
        this.jedisPool = config.getJedisPool();
    }

    public User findByUsername(String username) {
        try (Jedis jedis = jedisPool.getResource()) {
            String id = jedis.get(usernameKey(username));
            if (id == null) return null;
            return findById(id);
        }
    }

    public User findById(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> map = jedis.hgetAll(userKey(id));
            if (map == null || map.isEmpty()) return null;
            return fromMap(map);
        }
    }

    public User create(String username, String email, String passwordHash, String role) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.get(usernameKey(username)) != null) {
                throw new IllegalStateException("Username already exists");
            }
            long nextId = jedis.incr("user:id:seq");
            String id = String.valueOf(nextId);
            Map<String, String> data = new HashMap<>();
            data.put("id", id);
            data.put("username", username);
            data.put("email", email);
            data.put("passwordHash", passwordHash);
            data.put("role", role);
            jedis.hset(userKey(id), data);
            jedis.set(usernameKey(username), id);
            jedis.sadd("users:all", id);
            return new User(id, username, email, passwordHash, role);
        }
    }

    private String userKey(String id) {
        return "user:" + id;
    }

    private String usernameKey(String username) {
        return "user:username:" + username.toLowerCase();
    }

    private User fromMap(Map<String, String> map) {
        return new User(
                map.get("id"),
                map.get("username"),
                map.get("email"),
                map.get("passwordHash"),
                map.get("role")
        );
    }
}