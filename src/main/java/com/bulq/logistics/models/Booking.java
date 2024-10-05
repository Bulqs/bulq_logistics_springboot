package com.bulq.logistics.models;

import java.time.LocalDateTime;

import com.bulq.logistics.util.constants.PickupType;
import com.bulq.logistics.util.constants.ShipmentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    private String delivery_status;//PENDING,DELIVERED,CANCELLED

    private String sender_firstname;

    private String sender_lastname;

    private String sender_phoneNumber;

    private String sender_address;

    private String sender_email;

    private String sender_city;

    private String sender_country;

    private String sender_state;

    private String receiver_state;

    private String sender_lga;

    private String receiver_firstname;

    private String receiver_lastname;

    private String receiver_phoneNumber;

    private String receiver_address;

    private String receiver_email;

    private String receiver_city;

    private String receiver_country;

    private String receiver_lga;

    private String firstname;

    private String lastname;

    private String phoneNumber;

    private String address;

    private String email;

    private String city;

    private String country;

    private String lga;

    private LocalDateTime createdAt;

    private String package_name;

    private Integer weight;

    private String package_description;

    private String package_image;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_type", columnDefinition = "enum('EXPRESS', 'STANDARD') DEFAULT 'EXPRESS'")
    private ShipmentType shipment_type;

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_type", columnDefinition = "enum('M2A', 'A2M', 'SA2', 'PUO') DEFAULT 'PUO'")
    private PickupType pickupType;

    private String shipping_amount;

    private String pick_up_date;

    private String pick_up_time;

    private String dropoff_date;

    private String dropoff_time;

    private String deliveryId;

    //Many to one relationship with user
    @ManyToOne
    @JoinColumn(name="account_id", referencedColumnName="id", nullable=true)
    private Account account;

    // One-to-one relationship with Txn. (mapped by the 'booking' field in Transaction)
    @OneToOne(mappedBy = "booking")
    private Transaction transaction;
}
