package com.bulq.logistics.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bulq.logistics.models.KYCVerification;

public interface KYCVerificationRepository extends JpaRepository<KYCVerification, Long> {
    
}
