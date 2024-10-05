package com.bulq.logistics.payload.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelPayloadDTO {
    @NotBlank
    @Schema(defaultValue = "CANCEL", description = "cancel your order")
    private String cancel;
}
