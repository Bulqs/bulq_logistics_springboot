package com.bulq.logistics.payload.serviceCatalog;

import com.bulq.logistics.util.constants.ServiceCatalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddServiceCategoryPayloadDTO {
    
    private ServiceCatalog serviceCode;//"PUP", "DP", "BADO"

    
    private String serviceName;

    
    private String serviceDescription;

    
    private Integer price;
}
