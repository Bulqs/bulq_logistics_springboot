package com.bulq.logistics.payload.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterBookingViewDTO {
    
    private Long id;

    private String delivery_id;

    private String sender_lastname;

    private String sender_phoneNumber;

    private String receiver_phoneNumber;

    private String receiver_address;

    private String receiver_email;

    private String phoneNumber;

    private String address;

    private String email;

    private String city;

    private String country;

    private String lga;

    private String package_name;

    private Integer weight;

    private String package_description;

    private String shipment_type;

    private String pickupType;

    private String shipping_amount;

    private String pick_up_date;

    private String pick_up_time;

    private String dropoff_date;

    private String dropoff_time;
}
