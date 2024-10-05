package com.bulq.logistics.payload.wallet;

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
public class CreateWalletPayloadDTO {

    @NotBlank
    @Schema(defaultValue = "my wallet name", description = "RedOx98")
    private String name;
}
