package com.bulq.logistics.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bulq.logistics.models.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    @Query("SELECT r FROM Rating r WHERE " +
    "(:id IS NULL OR r.id = :id) " +  // Direct comparison for id (Long)
    "OR (:username IS NULL OR r.username LIKE CONCAT('%', :username, '%')) " +  // Handle username filter
    "OR (:stars IS NULL OR r.stars = :stars)")  // Exact match for stars (as String)
Page<Rating> findByRatingQueries(
    @Param("id") Long id,
    @Param("username") String username,
    @Param("stars") String stars,
    Pageable pageable
);


}
