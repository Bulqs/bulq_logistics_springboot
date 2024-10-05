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
public class AddHubPhonePayloadDTO {

    @Schema(defaultValue = "1", description = "input the hub id")
    private Long hubId;

    @NotBlank
    @Schema(defaultValue = "0814326373", description = "input the telephone")
    private String telephone;
}

/**
 * 
 * @Param("id") Long id,
 * @Param("state") String state,
 * @Param("city") String city,
 * @Param("country") String country,
 * Pageable pageable
 */
