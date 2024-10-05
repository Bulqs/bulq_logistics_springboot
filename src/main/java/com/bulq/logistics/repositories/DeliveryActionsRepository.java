package com.bulq.logistics.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bulq.logistics.models.DeliveryActions;

public interface DeliveryActionsRepository extends JpaRepository<DeliveryActions, Long> {
    List<DeliveryActions> findByBooking_DeliveryId(String deliveryId);
}
