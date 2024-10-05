package com.bulq.logistics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bulq.logistics.models.DeliveryActions;
import com.bulq.logistics.repositories.DeliveryActionsRepository;

@Service
public class DeliveryActionsService {
    @Autowired
    private DeliveryActionsRepository actionsRepository;

    public DeliveryActions save(DeliveryActions actions){
        return actionsRepository.save(actions);
    }

    public List<DeliveryActions> findByDeliveryId(String deliveryId) {
        return actionsRepository.findByBooking_DeliveryId(deliveryId);
    }

    
}
