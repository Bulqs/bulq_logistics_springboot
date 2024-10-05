package com.bulq.logistics.payload.serviceCatalog;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteCatalogPayloadDTO {

    @NotBlank
    private Long id;
}
