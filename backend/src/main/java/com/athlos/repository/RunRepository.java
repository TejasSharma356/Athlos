package com.athlos.repository;

import com.athlos.entity.Run;
import com.athlos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RunRepository extends JpaRepository<Run, Long> {
    List<Run> findByUserOrderByStartTimeDesc(User user);
    
    Optional<Run> findByUserAndIsActiveTrue(User user);
    
    @Query("SELECT r FROM Run r WHERE r.user = :user AND r.startTime >= :startDate AND r.startTime <= :endDate ORDER BY r.startTime DESC")
    List<Run> findByUserAndDateRange(@Param("user") User user, 
                                    @Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(r.totalSteps) FROM Run r WHERE r.user = :user AND r.startTime >= :startDate AND r.startTime <= :endDate")
    Integer getTotalStepsByUserAndDateRange(@Param("user") User user, 
                                          @Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(r.distanceMeters) FROM Run r WHERE r.user = :user AND r.startTime >= :startDate AND r.startTime <= :endDate")
    Double getTotalDistanceByUserAndDateRange(@Param("user") User user, 
                                            @Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(r.durationSeconds) FROM Run r WHERE r.user = :user AND r.startTime >= :startDate AND r.startTime <= :endDate")
    Long getTotalRunTimeByUserAndDateRange(@Param("user") User user, 
                                          @Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
}

