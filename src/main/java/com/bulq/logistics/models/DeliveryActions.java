package com.bulq.logistics.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DeliveryActions {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    private String activity;

    private LocalDateTime createdAt;

    //Many to one relationship with Delivery
    @ManyToOne
    @JoinColumn(name="delivery_id", referencedColumnName="deliveryId", nullable=true)
    private Booking booking;
}
