package com.bulq.logistics.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bulq.logistics.models.Rating;
import com.bulq.logistics.repositories.RatingRepository;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public Rating save(Rating category){
        return ratingRepository.save(category);
    }

    public List<Rating> findAll(){
        return ratingRepository.findAll();
    }

    public Optional<Rating> findById(Long id){
        return ratingRepository.findById(id);
    }

    public void deleteById (Long id){
        ratingRepository.deleteById(id);
    }

    public Page<Rating> findByRatingInfo(int page, int pageSize, String sortBy, Long id, String username, String stars) {
        // Check if sortBy is valid, or use a default field if necessary
        String validSortBy = (sortBy != null && !sortBy.isEmpty()) ? sortBy : "createdAt";
        
        // Create pageable object with sorting
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, validSortBy));
        
        // Ensure that id, username, and stars can be nullable in the query
        return ratingRepository.findByRatingQueries(id, username, stars, pageable);
    }
    
}
