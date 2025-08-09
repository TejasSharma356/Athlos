package com.leaderboard.model;

public class User {
    private String id;
    private String username;
    private String email;
    private String passwordHash;
    private String role; // ADMIN or USER

    public User() {}

    public User(String id, String username, String email, String passwordHash, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
}