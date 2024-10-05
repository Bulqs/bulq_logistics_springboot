package com.bulq.logistics.payload.card;

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
public class AddCardPayloadDTO {
    
    @Schema(description = "input wallet id", defaultValue = "1")
    private Long walletId;

    @NotBlank
    @Schema(description = "input your card number", defaultValue = "4444 4444 4444 4444")
    private String card_number;

    @NotBlank
    @Schema(description = "input your card expiry date", defaultValue = "06/25")
    private String expiry_date;

    @NotBlank
    @Schema(description = "input your card cvv", defaultValue = "123")
    private String cvv;
}
