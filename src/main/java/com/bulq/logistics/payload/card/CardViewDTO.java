package com.bulq.logistics.payload.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardViewDTO {
    
    private Long id;

    private String card_number;

    private String expiry_date;

    private String cvv;
}
