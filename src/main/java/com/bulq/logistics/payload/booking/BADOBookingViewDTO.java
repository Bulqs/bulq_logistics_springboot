package com.bulq.logistics.payload.booking;

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
public class BADOBookingViewDTO {

    private Long id;

    private String delivery_id;

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
}
