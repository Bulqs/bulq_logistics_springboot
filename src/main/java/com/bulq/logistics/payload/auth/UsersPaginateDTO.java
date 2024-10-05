package com.bulq.logistics.payload.auth;

import org.springframework.data.repository.query.Param;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersPaginateDTO {

    private String sort_by;

    private String per_page;

    private String page;
    
    @Schema(description = "Email address", example = "example@gmail.com", requiredMode = RequiredMode.REQUIRED)
    private String email;
    
    private String username;

    private String name;

    private String authorities;

    private String phoneNumber;

}
