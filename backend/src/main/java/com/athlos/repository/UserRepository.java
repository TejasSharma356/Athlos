package com.athlos.repository;

import com.athlos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    
    @Query("SELECT u FROM User u ORDER BY u.lastActive DESC")
    List<User> findAllOrderByLastActiveDesc();
    
    @Query("SELECT u FROM User u WHERE u.name ILIKE %:searchTerm% OR u.email ILIKE %:searchTerm%")
    List<User> findByNameOrEmailContainingIgnoreCase(@Param("searchTerm") String searchTerm);
}

