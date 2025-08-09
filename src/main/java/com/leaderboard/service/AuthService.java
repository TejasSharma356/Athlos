package com.leaderboard.service;

import com.leaderboard.config.RedisConfig;
import com.leaderboard.model.User;
import com.leaderboard.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(RedisConfig config) {
        this.userRepository = new UserRepository(config);
    }

    public User register(String username, String email, String rawPassword, String role) {
        validateNotEmpty(username, "Username");
        validateNotEmpty(email, "Email");
        validateNotEmpty(rawPassword, "Password");
        if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("USER")) {
            role = "USER";
        }
        String hash = hashPassword(rawPassword);
        return userRepository.create(username.trim(), email.trim(), hash, role.toUpperCase());
    }

    public User login(String username, String rawPassword) {
        validateNotEmpty(username, "Username");
        validateNotEmpty(rawPassword, "Password");
        User user = userRepository.findByUsername(username.trim());
        if (user == null) return null;
        String expected = user.getPasswordHash();
        String actual = hashPassword(rawPassword);
        if (expected != null && expected.equals(actual)) {
            return user;
        }
        return null;
    }

    public User findByUsername(String username) {
        if (username == null) return null;
        return userRepository.findByUsername(username.trim());
    }

    public User findById(String id) { return userRepository.findById(id); }

    private void validateNotEmpty(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " cannot be empty");
        }
    }

    private String hashPassword(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] salt = "DEMO_SALT".getBytes(StandardCharsets.UTF_8);
            digest.update(salt);
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}