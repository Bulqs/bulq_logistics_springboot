package com.bulq.logistics.payload.hub;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllHubFullViewDTO {

    private Long id;

    private String state;

    private String city;

    private String country;

    private String address;
    
    private List<HubFullViewDTO> hubs;
}
