package com.bulq.logistics.payload.hub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HubViewDTO {
    private Long id;

    private String state;

    private String city;

    private String country;

    private String address;
}
