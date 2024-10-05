package com.bulq.logistics.payload.complaint;

import com.bulq.logistics.util.constants.ComplaintType;

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
public class UpdateComplaintPayloadDTO {

    @Schema(description = "input the complaintId", defaultValue = "1")
    private Long complaintId;

    @Schema(description = "input the resolution", defaultValue = "resolving the matter")
    private String resolution;

    @Schema(description = "input complaint status", defaultValue = "OPEN")
    private ComplaintType complaint_status;
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