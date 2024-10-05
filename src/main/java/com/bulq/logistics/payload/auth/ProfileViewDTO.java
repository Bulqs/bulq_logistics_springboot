package com.bulq.logistics.payload.auth;

import java.time.LocalDateTime;
import java.util.List;

import com.bulq.logistics.payload.booking.GeneralBookingViewDTO;
import com.bulq.logistics.util.constants.Verification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileViewDTO {
    
    private Long id;

    private String email;

    private String authorities;

    private LocalDateTime createdAt;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String username;

    private String verified;

    private List<GeneralBookingViewDTO> bookings;

}
