package com.leaderboard.config;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig {
    private static RedisConfig instance;
    private final JedisPool jedisPool;

    private RedisConfig() {
        String host = System.getenv().getOrDefault("REDIS_HOST", "localhost");
        int port = Integer.parseInt(System.getenv().getOrDefault("REDIS_PORT", "6379"));
        String password = System.getenv().getOrDefault("REDIS_PASSWORD", "");

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(16);
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        if (password != null && !password.isEmpty()) {
            this.jedisPool = new JedisPool(poolConfig, host, port, 2000, password);
        } else {
            this.jedisPool = new JedisPool(poolConfig, host, port, 2000);
        }
    }

    public static synchronized RedisConfig getInstance() {
        if (instance == null) {
            instance = new RedisConfig();
        }
        return instance;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }
}