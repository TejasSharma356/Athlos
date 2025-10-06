package com.athlos.repository;

import com.athlos.entity.Territory;
import com.athlos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerritoryRepository extends JpaRepository<Territory, Long> {
    List<Territory> findByUserOrderByClaimedAtDesc(User user);
    
    List<Territory> findByIsActiveTrue();
    
    @Query("SELECT COUNT(t) FROM Territory t WHERE t.user = :user AND t.isActive = true")
    Integer countActiveTerritoriesByUser(@Param("user") User user);
    
    @Query("SELECT SUM(t.areaSquareMeters) FROM Territory t WHERE t.user = :user AND t.isActive = true")
    Double getTotalAreaByUser(@Param("user") User user);
    
    @Query(value = "SELECT t.* FROM territories t WHERE t.is_active = true AND ST_Intersects(t.polygon, ST_GeomFromText(:wkt, 4326))", nativeQuery = true)
    List<Territory> findIntersectingTerritories(@Param("wkt") String wkt);
}

