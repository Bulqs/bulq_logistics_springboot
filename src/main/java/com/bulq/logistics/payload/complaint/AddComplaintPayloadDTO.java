package com.bulq.logistics.payload.complaint;

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
public class AddComplaintPayloadDTO {

    @NotBlank
    @Schema(description = "input your complaint", defaultValue = "I'd like to complain about...")
    private String complaint;

    @NotBlank
    @Schema(description = "input your complainant/user name", defaultValue = "my name")
    private String complainant;

    @NotBlank
    @Schema(description = "input your delivery_code", defaultValue = "12345")
    private String delivery_code;

    // @NotBlank
    // @Schema(description = "input your card cvv", defaultValue = "123")
    // private String complaint_status;
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