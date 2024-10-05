package com.bulq.logistics.payload.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeductWalletPayloadDTO {
    
    @Schema(defaultValue = "walletId", description = "provide your walletId",requiredMode = RequiredMode.REQUIRED)
    private Long walletId;

    @Schema(defaultValue = "bookingId", description = "provide your bookingId",requiredMode = RequiredMode.REQUIRED)
    private Long bookingId;

    @NotBlank
    @Schema(defaultValue = "0.00", description = "provide your walletId",requiredMode = RequiredMode.REQUIRED)
    private String amount;
}
