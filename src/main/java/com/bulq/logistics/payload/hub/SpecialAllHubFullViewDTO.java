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
public class SpecialAllHubFullViewDTO {
    
    private List<AllHubFullViewDTO> hubs;
}
