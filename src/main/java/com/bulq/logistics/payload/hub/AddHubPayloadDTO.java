package com.bulq.logistics.payload.hub;

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
public class AddHubPayloadDTO {

    @NotBlank
    @Schema(defaultValue = "lagos", description = "input the state")
    private String state;

    @NotBlank
    @Schema(defaultValue = "VI", description = "input the city")
    private String city;

    @NotBlank
    @Schema(defaultValue = "Nigeria", description = "input the country")
    private String country;

    @NotBlank
    @Schema(defaultValue = "10 Address.", description = "input the address")
    private String address;
}

/**
 * 
 * @Param("id") Long id,
 * @Param("state") String state,
 * @Param("city") String city,
 * @Param("country") String country,
 * Pageable pageable
 */
