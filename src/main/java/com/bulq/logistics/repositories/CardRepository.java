package com.bulq.logistics.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bulq.logistics.models.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByWallet_id(Long id);
}
