package com.bulq.logistics.payload.notification;

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
public class AddNotificationPayloadDTO {
    
    @Schema(description = "input account id", defaultValue = "1")
    private Long accountId;

    @NotBlank
    @Schema(description = "input message", defaultValue = "notification message")
    private String message;
}
/**
 * 
 * 
 * private String image;

    private String message;
 */