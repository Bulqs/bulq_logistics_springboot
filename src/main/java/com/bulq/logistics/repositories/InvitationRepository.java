package com.bulq.logistics.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bulq.logistics.models.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    
}
