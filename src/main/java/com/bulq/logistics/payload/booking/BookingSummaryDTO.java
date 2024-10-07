package com.bulq.logistics.payload.booking;

import com.bulq.logistics.util.constants.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingSummaryDTO {
    
    private String month;
    private Integer year;
    private Integer day;
    private Status status;
    private Long totalItems;  // Total number of items for the given status
}
