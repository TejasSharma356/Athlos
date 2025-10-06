package com.athlos.controller;

import com.athlos.dto.UserDTO;
import com.athlos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserRegistrationRequest request) {
        try {
            UserDTO user = userService.createUser(request.getEmail(), request.getPassword(), request.getName());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserLoginRequest request) {
        Optional<UserDTO> user = userService.authenticateUser(request.getEmail(), request.getPassword());
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        Optional<UserDTO> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/location")
    public ResponseEntity<Void> updateLocation(@PathVariable Long id, @RequestBody LocationUpdateRequest request) {
        try {
            userService.updateUserLocation(id, request.getLatitude(), request.getLongitude());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String q) {
        List<UserDTO> users = userService.searchUsers(q);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    // Request DTOs
    public static class UserRegistrationRequest {
        private String email;
        private String password;
        private String name;
        
        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
    
    public static class UserLoginRequest {
        private String email;
        private String password;
        
        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class LocationUpdateRequest {
        private Double latitude;
        private Double longitude;
        
        // Getters and Setters
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }
}

