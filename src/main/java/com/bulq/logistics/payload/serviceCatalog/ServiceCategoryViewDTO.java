package com.bulq.logistics.payload.serviceCatalog;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceCategoryViewDTO {

    @NotBlank
    @Schema(description = "Catalog ID", example = "1",
    requiredMode = RequiredMode.REQUIRED)
    private Long id;

    @NotBlank
    @Schema(description = "Service Code", example = "PUP",
    requiredMode = RequiredMode.REQUIRED)
    private String serviceCode;//"PUP", "DP", "BADO"

    @NotBlank
    @Schema(description = "Service Code", example = "PUP",
    requiredMode = RequiredMode.REQUIRED)
    private String serviceName;

    @NotBlank
    @Schema(description = "Service Code", example = "PUP",
    requiredMode = RequiredMode.REQUIRED)
    private String serviceDescription;

    private Integer price;
}
