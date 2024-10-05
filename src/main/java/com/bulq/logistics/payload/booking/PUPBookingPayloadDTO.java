package com.bulq.logistics.payload.booking;

import com.bulq.logistics.util.constants.ServiceType;
import com.bulq.logistics.util.constants.ShipmentType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PUPBookingPayloadDTO {
    

    // @NotBlank
    // @Schema(defaultValue = "sender lastName", description = "Input sender lastname")
    private String sender_firstname;

    // @NotBlank
    @Schema(defaultValue = "sender lastName", description = "Input sender lastname")
    private String sender_lastname;

    // @NotBlank
    @Schema(defaultValue = "sender lastName", description = "Input sender lastname")
    private String sender_phoneNumber;

    // @NotBlank
    @Schema(defaultValue = "sender lastName", description = "Input sender lastname")
    private String sender_address;

    // @NotBlank
    @Schema(defaultValue = "sender lastName", description = "Input sender lastname")
    private String sender_email;

    // @NotBlank
    @Schema(defaultValue = "sender lastName", description = "Input sender lastname")
    private String sender_city;

    // @NotBlank
    @Schema(defaultValue = "sender lastName", description = "Input sender lastname")
    private String sender_country;

    // @NotBlank
    @Schema(defaultValue = "sender lastName", description = "Input sender lastname")
    private String sender_lga;

    // @NotBlank
    @Schema(defaultValue = "receiver lastName", description = "Input sender lastname")
    private String receiver_firstname;

    // @NotBlank
    @Schema(defaultValue = "receiver lastName", description = "Input sender lastname")
    private String receiver_lastname;

    // @NotBlank
    @Schema(defaultValue = "receiver lastName", description = "Input sender lastname")
    private String receiver_phoneNumber;

    // @NotBlank
    @Schema(defaultValue = "receiver lastName", description = "Input sender lastname")
    private String receiver_address;

    // @NotBlank
    @Schema(defaultValue = "receiver lastName", description = "Input sender lastname")
    private String receiver_email;

    // @NotBlank
    @Schema(defaultValue = "receiver lastName", description = "Input sender lastname")
    private String receiver_city;

    // @NotBlank
    @Schema(defaultValue = "receiver lastName", description = "Input sender lastname")
    private String receiver_country;

    // @NotBlank
    @Schema(defaultValue = "sender state", description = "enter the sender state")
    private String sender_state;

    // @NotBlank
    @Schema(defaultValue = "receiver state", description = "enter the receiver state")
    private String receiver_state;

    // @NotBlank
    @Schema(defaultValue = "receiver lastName", description = "Input sender lastname")
    private String receiver_lga;

    // @NotBlank
    @Schema(defaultValue = "food", description = "enter your package name")
    private String package_name;

    // @NotBlank
    @Schema(defaultValue = "10", description = "enter your package weight")
    private String weight;

    // @NotBlank
    @Schema(defaultValue = "10", description = "describe your package")
    private String package_description;

    // @NotBlank
    @Schema(defaultValue = "image", description = "Enter the photo of the package")
    private String package_image;

    private ShipmentType shipment_type;

   
    private ServiceType pickupType;

    // @NotBlank
    @Schema(defaultValue = "30", description = "Shipping Amount")
    private String shipping_amount;

    // @NotBlank
    @Schema(defaultValue = "date", description = "Pick up date")
    private String pick_up_date;

    // @NotBlank
    @Schema(defaultValue = "time", description = "Pick up time")
    private String pick_up_time;

    // @NotBlank
    @Schema(defaultValue = "time", description = "drop off time")
    private String dropoff_date;

    // @NotBlank
    @Schema(defaultValue = "time", description = "drop off time")
    private String dropoff_time;
}
