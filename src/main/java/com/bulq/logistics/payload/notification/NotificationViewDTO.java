package com.bulq.logistics.payload.notification;

import com.bulq.logistics.util.constants.ReadStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationViewDTO {
    
    private Long accountId;

    private String image;

    private String message;

    private ReadStatus status;
}
/**
 * 
 * 
 * private String image;

    private String message;
 */