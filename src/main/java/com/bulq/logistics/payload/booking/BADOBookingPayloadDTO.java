package com.bulq.logistics.payload.booking;

import com.bulq.logistics.util.constants.ServiceType;
import com.bulq.logistics.util.constants.ShipmentType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BADOBookingPayloadDTO {

    @NotBlank
    @Schema(defaultValue = "firstname", description = "Input receiver's lastname")
    private String firstname;

    @NotBlank
    @Schema(defaultValue = "lastName", description = "Input receiver's lastname")
    private String lastname;

    @NotBlank
    @Schema(defaultValue = "+234XXXXX", description = "Input receiver's PhoneNumber")
    private String phoneNumber;

    @NotBlank
    @Schema(defaultValue = "Address", description = "Enter receiver's Address")
    private String address;

    @NotBlank
    @Schema(defaultValue = "name@gmail.com", description = "enter receiver email address")
    private String email;

    @NotBlank
    @Schema(defaultValue = "city", description = "enter receiver's city")
    private String city;

    @NotBlank
    @Schema(defaultValue = "country", description = "enter receiver's country")
    private String country;

    @NotBlank
    @Schema(defaultValue = "country", description = "enter receiver's country")
    private String state;

    @NotBlank
    @Schema(defaultValue = "country", description = "enter receiver's local goverment area")
    private String lga;

    @NotBlank
    @Schema(defaultValue = "food", description = "enter the package name")
    private String package_name;

    @NotBlank
    @Schema(defaultValue = "10", description = "enter the package weight")
    private String weight;

    @NotBlank
    @Schema(defaultValue = "10", description = "describe the package")
    private String package_description;

    @NotBlank
    @Schema(defaultValue = "image", description = "Enter the photo of the package")
    private String package_image;

    // @NotBlank
    // @Schema(defaultValue = "10", description = "choose between STANDARD or EXPRESS")
    private ShipmentType shipment_type;

    // @NotBlank
    // @Schema(defaultValue = "PUO", description = "choose between SA2 or A2M, M2A, PUO")
    private ServiceType pickupType;

    @NotBlank
    @Schema(defaultValue = "30", description = "Shipping Amount")
    private String shipping_amount;

    @NotBlank
    @Schema(defaultValue = "date", description = "Pick up date")
    private String pick_up_date;

    @NotBlank
    @Schema(defaultValue = "time", description = "Pick up time")
    private String pick_up_time;

    @NotBlank
    @Schema(defaultValue = "time", description = "drop off time")
    private String dropoff_date;

    @NotBlank
    @Schema(defaultValue = "time", description = "drop off time")
    private String dropoff_time;
}
