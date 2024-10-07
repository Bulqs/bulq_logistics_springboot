package com.bulq.logistics.payload.notification;

import com.bulq.logistics.util.constants.ReadStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNotificationPayloadDTO {
    
    
    @Schema(description = "input message", defaultValue = "READ")
    private ReadStatus status;
}
/**
 * 
 * 
 * private String image;

    private String message;
 */