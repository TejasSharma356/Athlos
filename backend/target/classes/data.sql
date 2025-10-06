-- Initialize PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Create sample users for testing (passwords are 'password123' hashed with BCrypt)
INSERT INTO users (email, password, name, age, gender, daily_step_goal, created_at, last_active) VALUES
('john@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'John Doe', 25, 'Male', 8000, NOW(), NOW()),
('jane@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Jane Smith', 28, 'Female', 7000, NOW(), NOW()),
('mike@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Mike Johnson', 30, 'Male', 9000, NOW(), NOW()),
('sarah@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Sarah Wilson', 26, 'Female', 7500, NOW(), NOW()),
('alex@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Alex Brown', 24, 'Non-binary', 6000, NOW(), NOW());

-- Create sample runs for testing
INSERT INTO runs (user_id, start_time, end_time, duration_seconds, total_steps, distance_meters, is_active) VALUES
(1, NOW() - INTERVAL '2 hours', NOW() - INTERVAL '1 hour', 3600, 4500, 3200.5, false),
(2, NOW() - INTERVAL '1 hour', NOW() - INTERVAL '30 minutes', 1800, 2800, 2100.0, false),
(3, NOW() - INTERVAL '30 minutes', NULL, NULL, 1500, 1200.0, true),
(4, NOW() - INTERVAL '3 hours', NOW() - INTERVAL '2 hours', 3600, 5200, 3800.0, false),
(5, NOW() - INTERVAL '4 hours', NOW() - INTERVAL '3 hours', 3600, 4800, 3500.0, false);

