package com.bulq.logistics.payload.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddRatingPayloadDTO {
    
    // @Schema(description = "input wallet id", defaultValue = "1")
    // private Long walletId;

    @NotBlank
    @Schema(description = "input the comment", defaultValue = "hello", 
    requiredMode = RequiredMode.REQUIRED)
    private String comment;

    @NotBlank
    @Schema(description = "input the rating", defaultValue = "5", 
    requiredMode = RequiredMode.REQUIRED, minLength = 1, maxLength = 1)
    private String stars;
}

/**
 * 
 * 
 * private long id;

    private String username;

    private String userImage;

    private String comment;

    private String stars;
 */
