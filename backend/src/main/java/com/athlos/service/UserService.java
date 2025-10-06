package com.athlos.service;

import com.athlos.dto.UserDTO;
import com.athlos.entity.User;
import com.athlos.repository.UserRepository;
import com.athlos.service.PasswordEncoderService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoderService passwordEncoderService;
    
    private final GeometryFactory geometryFactory = new GeometryFactory();
    
    public UserDTO createUser(String email, String password, String name) {
        User user = new User(email, passwordEncoderService.encodePassword(password), name);
        user = userRepository.save(user);
        return convertToDTO(user);
    }
    
    public Optional<UserDTO> authenticateUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoderService.matches(password, user.getPassword()))
                .map(this::convertToDTO);
    }
    
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDTO);
    }
    
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        
        if (userDTO.getName() != null) user.setName(userDTO.getName());
        if (userDTO.getAge() != null) user.setAge(userDTO.getAge());
        if (userDTO.getGender() != null) user.setGender(userDTO.getGender());
        if (userDTO.getDailyStepGoal() != null) user.setDailyStepGoal(userDTO.getDailyStepGoal());
        
        user.setLastActive(LocalDateTime.now());
        user = userRepository.save(user);
        return convertToDTO(user);
    }
    
    public void updateUserLocation(Long userId, Double latitude, Double longitude) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        user.setCurrentLocation(location);
        user.setLastActive(LocalDateTime.now());
        userRepository.save(user);
    }
    
    public List<UserDTO> searchUsers(String searchTerm) {
        return userRepository.findByNameOrEmailContainingIgnoreCase(searchTerm)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAllOrderByLastActiveDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setGender(user.getGender());
        dto.setDailyStepGoal(user.getDailyStepGoal());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setLastActive(user.getLastActive());
        
        if (user.getCurrentLocation() != null) {
            dto.setLatitude(user.getCurrentLocation().getY());
            dto.setLongitude(user.getCurrentLocation().getX());
        }
        
        return dto;
    }
}

