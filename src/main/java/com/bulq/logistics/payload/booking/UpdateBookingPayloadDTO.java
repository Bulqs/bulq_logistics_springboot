package com.bulq.logistics.payload.booking;

import com.bulq.logistics.util.constants.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingPayloadDTO {
    
    @Schema(defaultValue = "CANCEL OR PENDING OR COMPLETED", description = "cancel your order")
    private Status cancel;
}
