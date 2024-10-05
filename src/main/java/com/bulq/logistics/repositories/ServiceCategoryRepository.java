package com.bulq.logistics.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bulq.logistics.models.ServiceCategory;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    
}
