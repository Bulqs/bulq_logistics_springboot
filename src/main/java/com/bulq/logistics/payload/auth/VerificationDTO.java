package com.bulq.logistics.payload.auth;

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
public class VerificationDTO {

    @NotBlank(message = "can not be blank")
    @Schema(description = "Vefication links token", example = "adfsgdhfjkdlsl", requiredMode = RequiredMode.REQUIRED)
    private String verificationToken;
}
