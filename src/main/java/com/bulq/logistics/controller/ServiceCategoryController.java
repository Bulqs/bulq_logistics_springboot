package com.bulq.logistics.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bulq.logistics.models.ServiceCategory;
import com.bulq.logistics.payload.serviceCatalog.AddServiceCategoryPayloadDTO;
import com.bulq.logistics.payload.serviceCatalog.DeleteCatalogPayloadDTO;
import com.bulq.logistics.payload.serviceCatalog.ServiceCategoryPayloadDTO;
import com.bulq.logistics.repositories.ServiceCategoryRepository;
import com.bulq.logistics.services.ServiceCategoryService;
import com.bulq.logistics.util.constants.ServiceCatalog;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/service-catalog")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Service category Controller", description = "Controller for Service Category")
public class ServiceCategoryController {

    @Autowired
    private final ServiceCategoryService categoryService;

    @Autowired
    private final ServiceCategoryRepository categoryRepository;

    @PostMapping("/create-service")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "201", description = "Service Category added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new Service Category")
    // @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> addService(@Valid @RequestBody AddServiceCategoryPayloadDTO payloadDTO) {
        try {
            ServiceCategory category = new ServiceCategory();
            category.setServiceName(payloadDTO.getServiceName());
            category.setServiceCode(ServiceCatalog.fromValue(payloadDTO.getServiceCode().toString()));
            category.setPrice(payloadDTO.getPrice());
            category.setServiceDescription(payloadDTO.getServiceDescription());
            categoryService.save(category);

            return ResponseEntity.ok("Service Added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/catalogs", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "List of catalogs")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "View catalogs")
    // @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<List<ServiceCategoryPayloadDTO>> catalogs() {
        List<ServiceCategoryPayloadDTO> catalogs = new ArrayList<>();
        for (ServiceCategory catalog : categoryRepository.findAll()) {
            catalogs.add(new ServiceCategoryPayloadDTO(
                    catalog.getId(),
                    catalog.getServiceCode(),
                    catalog.getServiceName(),
                    catalog.getServiceDescription(),
                    catalog.getPrice()));
        }
        return ResponseEntity.ok(catalogs);
    }

    @PutMapping(value = "/update-catalog", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "List of catalogs")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "update catalog")
    // @SecurityRequirement(name="bulq-demo-api")
    public ResponseEntity<ServiceCategoryPayloadDTO> updateCatalog(
            @Valid @RequestBody ServiceCategoryPayloadDTO payloadDTO) {
        System.out.println(payloadDTO);
        Optional<ServiceCategory> optionalCategory = categoryService.findById(payloadDTO.getId());
        if (optionalCategory.isPresent()) {
            ServiceCategory category = optionalCategory.get();
            category.setServiceName(payloadDTO.getServiceName());
            category.setServiceDescription(payloadDTO.getServiceDescription());
            category.setPrice(payloadDTO.getPrice());
            categoryService.save(category);

            ServiceCategoryPayloadDTO categoryPayloadDTO = new ServiceCategoryPayloadDTO(
                    category.getId(),
                    category.getServiceCode(),
                    category.getServiceName(),
                    category.getServiceDescription(),
                    category.getPrice());
            return ResponseEntity.ok(categoryPayloadDTO);

        }
        return ResponseEntity.badRequest().body(null);
    }

    @DeleteMapping(value = "/delete-catalog")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Delete profile")
    // @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> deleteCatalog(@RequestBody DeleteCatalogPayloadDTO payloadDTO) {
        Optional<ServiceCategory> optionalCategory = categoryService.findById(payloadDTO.getId());
        if (optionalCategory.isPresent()) {
            ServiceCategory category = optionalCategory.get();
            categoryService.deleteById(category.getId());
            return ResponseEntity.ok("Catalog Deleted Successfully");

        }
        return ResponseEntity.badRequest().body(null);
    }

}
