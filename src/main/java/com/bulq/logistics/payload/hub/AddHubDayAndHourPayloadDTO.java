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
public class AddHubDayAndHourPayloadDTO {

    @Schema(defaultValue = "1", description = "input the hub id")
    private Long hubId;

    @NotBlank
    @Schema(defaultValue = "lagos", description = "input the date of the week")
    private String day;

    @NotBlank
    @Schema(defaultValue = "8:00AM - 5:00PM", description = "input the Working Hour of the day")
    private String time;
}

/**
 * 
 * @Param("id") Long id,
 * @Param("state") String state,
 * @Param("city") String city,
 * @Param("country") String country,
 * Pageable pageable
 * private String day;

    private String time;
 */
