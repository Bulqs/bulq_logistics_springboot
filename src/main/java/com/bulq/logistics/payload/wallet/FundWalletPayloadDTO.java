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
public class FundWalletPayloadDTO {
    
    @Schema(defaultValue = "walletId", description = "provide your walletId",requiredMode = RequiredMode.REQUIRED)
    private Long walletId;

    @NotBlank
    @Schema(defaultValue = "0.00", description = "provide your walletId",requiredMode = RequiredMode.REQUIRED)
    private String amount;
}
