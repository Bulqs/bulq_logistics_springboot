package com.bulq.logistics.payload.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteHubPayloadDTO {

    @NotBlank
    private Long id;
}
