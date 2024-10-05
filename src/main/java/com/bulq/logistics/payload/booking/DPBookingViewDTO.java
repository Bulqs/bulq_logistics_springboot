package com.bulq.logistics.payload.booking;

import com.bulq.logistics.models.Account;
import com.bulq.logistics.util.constants.ServiceType;
import com.bulq.logistics.util.constants.ShipmentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DPBookingViewDTO {
    
    private Long id;

    private String delivery_id;
    
    private String sender_firstname;

    private String sender_lastname;

    private String sender_phoneNumber;

    private String sender_address;

    private String sender_email;

    private String sender_city;

    private String sender_country;

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

    private String package_name;

    private String weight;

    private String package_description;

    private ShipmentType shipment_type;

    private ServiceType pickupType;

    private String shipping_amount;

    private String pick_up_date;

    private String pick_up_time;

    private String dropoff_date;

    private String dropoff_time;

    private Account account;
}
