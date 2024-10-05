package com.bulq.logistics.models;


import com.bulq.logistics.util.constants.ServiceCatalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ServiceCategory {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_category", columnDefinition = "enum('PUP', 'BADO', 'DP') DEFAULT 'PUP'")
    private ServiceCatalog serviceCode;//"PUP", "DP", "BADO"

    private String serviceName;

    private String serviceDescription;

    private Integer price;
}