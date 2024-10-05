package com.bulq.logistics.payload.complaint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintViewDTO {

    private Long id;
    
    private String complaint;

    private String complainant;

    private String delivery_code;

    private String complaint_status;
}

/**
 * 
 * 
 * private String complaint;

    private String complainant; //Name of complainant.

    private String delivery_code;

    @Enumerated(EnumType.STRING)
    @Column(name = "complaint_status", columnDefinition = "enum('OPEN', 'PENDING', 'RESOLVED', 'CLOSED') DEFAULT 'PENDING'")
    private ComplaintType complaint_status
 */