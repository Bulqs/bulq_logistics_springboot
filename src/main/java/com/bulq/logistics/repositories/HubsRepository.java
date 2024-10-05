package com.bulq.logistics.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bulq.logistics.models.Hub;

public interface HubsRepository extends JpaRepository<Hub, Long> {
    
    @Query("SELECT a FROM Hub a WHERE " +
    "(:id IS NULL OR a.id = :id) " +  // Use direct comparison for id (Long)
    "AND (:state IS NULL OR a.state LIKE CONCAT('%', :state, '%')) " +  // Removed duplicate condition
    "AND (:city IS NULL OR a.city LIKE CONCAT('%', :city, '%')) " +  // No need to check against `country` here
    "AND (:country IS NULL OR a.country LIKE CONCAT('%', :country, '%'))")  // Removed unnecessary OR condition
Page<Hub> findByHubInfo(
    @Param("id") Long id,
    @Param("state") String state,
    @Param("city") String city,
    @Param("country") String country,
    Pageable pageable
);

}
