package com.bulq.logistics.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bulq.logistics.models.Hub;
import com.bulq.logistics.models.Phone;
import com.bulq.logistics.models.WorkingHour;
import com.bulq.logistics.payload.hub.AddHubDayAndHourPayloadDTO;
import com.bulq.logistics.payload.hub.AddHubPayloadDTO;
import com.bulq.logistics.payload.hub.AddHubPhonePayloadDTO;
import com.bulq.logistics.payload.hub.DeleteHubPayloadDTO;
import com.bulq.logistics.payload.hub.HubFullViewDTO;
import com.bulq.logistics.payload.hub.HubViewDTO;
import com.bulq.logistics.payload.hub.PhoneViewDTO;
import com.bulq.logistics.payload.hub.UpdateHubPayloadDTO;
import com.bulq.logistics.payload.hub.WorkingHourViewDTO;
import com.bulq.logistics.repositories.HubsRepository;
import com.bulq.logistics.services.HubService;
import com.bulq.logistics.services.PhoneService;
import com.bulq.logistics.services.WorkHourService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/hubs")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Hubs Controller", description = "Controller for Hubs and their working hours of the week")
public class HubController {

    @Autowired
    private final HubService hubService;

    @Autowired
    private final HubsRepository hubsRepository;

    @Autowired
    private final PhoneService phoneService;

    @Autowired
    private final WorkHourService workHourService;

    @PostMapping("/create-hub")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "201", description = "Service Category added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new Hub")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> addService(@Valid @RequestBody AddHubPayloadDTO payloadDTO) {
        try {
            Hub hub = new Hub();
            hub.setCountry(payloadDTO.getCountry());
            hub.setState(payloadDTO.getState());
            hub.setCity(payloadDTO.getCity());
            hub.setAddress(payloadDTO.getAddress());
            hub.setCreatedAt(LocalDateTime.now());
            hubService.save(hub);

            return ResponseEntity.ok("Hub Added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Issue with adding hub. Only admins can add hub.");
        }
    }

    @GetMapping(value = "/all", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "List of catalogs")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "View hubs")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<List<HubViewDTO>> catalogs() {
        List<HubViewDTO> hubs = new ArrayList<>();
        for (Hub hub : hubsRepository.findAll()) {
            hubs.add(new HubViewDTO(hub.getId(), hub.getState(), hub.getCity(), hub.getCountry(), hub.getAddress()));
        }
        return ResponseEntity.ok(hubs);
    }

    @PutMapping(value = "/update-hub", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "List of catalogs")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "update hub")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<HubViewDTO> updateCatalog(@Valid @RequestBody UpdateHubPayloadDTO payloadDTO) {
        System.out.println(payloadDTO);
        Optional<Hub> optionalHub = hubService.findById(payloadDTO.getId());
        if (optionalHub.isPresent()) {
            Hub hub = optionalHub.get();
            hub.setCountry(payloadDTO.getCountry());
            hub.setState(payloadDTO.getState());
            hub.setCity(payloadDTO.getCity());
            hub.setAddress(payloadDTO.getAddress());
            hubService.save(hub);

            HubViewDTO hubViewDTO = new HubViewDTO(hub.getId(), hub.getState(), hub.getCity(), hub.getCountry(),
                    hub.getAddress());
            return ResponseEntity.ok(hubViewDTO);

        }
        return ResponseEntity.badRequest().body(null);
    }

    @DeleteMapping(value = "/delete-hub")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Delete hub")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> deleteCatalog(@RequestBody DeleteHubPayloadDTO payloadDTO) {
        Optional<Hub> optionalhub = hubService.findById(payloadDTO.getId());
        if (optionalhub.isPresent()) {
            Hub hub = optionalhub.get();
            hubService.deleteById(hub.getId());
            return ResponseEntity.ok("Hub Deleted Successfully");

        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/create-hub-workinghours")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "201", description = "Service Category added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new working hours to a Hub")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> addHubAndWorkingHours(@Valid @RequestBody AddHubDayAndHourPayloadDTO payloadDTO) {
        Optional<Hub> optionalHub = hubService.findById(payloadDTO.getHubId());
        Hub hub;
        if (!optionalHub.isPresent()) {
            return ResponseEntity.badRequest().body("Hub doesn't exist");
        }
        hub = optionalHub.get();
        try {
            WorkingHour workingHour = new WorkingHour();
            workingHour.setDay(payloadDTO.getDay());
            workingHour.setTime(payloadDTO.getTime());
            workingHour.setHub(hub);
            workHourService.save(workingHour);

            return ResponseEntity.ok("Work hour added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Issue with adding hub. Only admins can add hub.");
        }
    }

    @PostMapping("/add-hub-telephone")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "201", description = "Service Category added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new telephone to a hub")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> addHubTelephone(@Valid @RequestBody AddHubPhonePayloadDTO payloadDTO) {
        Optional<Hub> optionalHub = hubService.findById(payloadDTO.getHubId());
        Hub hub;
        if (!optionalHub.isPresent()) {
            return ResponseEntity.badRequest().body("Hub doesn't exist");
        }
        hub = optionalHub.get();
        try {
            Phone phone = new Phone();
            phone.setTelephone(payloadDTO.getTelephone());
            phone.setHub(hub);
            phoneService.save(phone);

            return ResponseEntity.ok("Telephone added to hub successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Issue with adding hub. Only admins can add hub.");
        }
    }

    @GetMapping(value = "/{hubId}", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "delivery code found success")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "Find a hub detail")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<HubFullViewDTO> findSingleHub(@PathVariable("hubId") Long hubId,
            Authentication authentication) {
        Optional<Hub> optionalHub = hubService.findById(hubId);

        if (!optionalHub.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        Hub hub;
        hub = optionalHub.get();

        List<PhoneViewDTO> phone = new ArrayList<>();
        List<WorkingHourViewDTO> workHour = new ArrayList<>();

        for (WorkingHour workingHour : workHourService.findByHub_id(hubId)) {
            workHour.add(new WorkingHourViewDTO(workingHour.getId(), workingHour.getDay(), workingHour.getTime()));
        }

        for (Phone telephone : phoneService.findByHub_id(hubId)) {
            phone.add(new PhoneViewDTO(telephone.getId(), telephone.getTelephone()));
        }

        HubFullViewDTO hubFullViewDTO = new HubFullViewDTO(hub.getId(), hub.getState(), hub.getCity(), hub.getCountry(),
                hub.getAddress(), phone, workHour);

        return ResponseEntity.ok(hubFullViewDTO);
    }

    @GetMapping("/")
    @ApiResponse(responseCode = "200", description = "List of users pagination")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List users in paginated format")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<List<HubFullViewDTO>> allUsers(
            @RequestParam(required = false, name = "createdAt", defaultValue = "createdAt") String sort_by,
            @RequestParam(required = false, name = "per_page", defaultValue = "2") String per_page,
            @RequestParam(required = false, name = "page", defaultValue = "1") String page,
            @RequestParam(required = false, name = "id", defaultValue = "1") Long id,
            @RequestParam(required = false, name = "state", defaultValue = "lagos") String state,
            @RequestParam(required = false, name = "city", defaultValue = "") String city,
            @RequestParam(required = false, name = "country", defaultValue = "Nigeria") String country) {

        // Fetch paginated data
        Page<Hub> hubsOnPage = hubService.findByHubInfo(Integer.parseInt(page) - 1,
                Integer.parseInt(per_page), sort_by, id, state, city, country);

        List<Hub> hubList = hubsOnPage.getContent();
        System.out.println(hubList);

        // Initialize the lists for DTOs
        List<HubFullViewDTO> hubFullViewList = new ArrayList<>();

        for (Hub hub : hubList) {
            // Fetch working hours and phones for each hub
            List<WorkingHourViewDTO> workHour = workHourService.findByHub_id(hub.getId())
                    .stream()
                    .map(wh -> new WorkingHourViewDTO(wh.getId(), wh.getDay(), wh.getTime()))
                    .collect(Collectors.toList());

            List<PhoneViewDTO> phone = phoneService.findByHub_id(hub.getId())
                    .stream()
                    .map(ph -> new PhoneViewDTO(ph.getId(), ph.getTelephone()))
                    .collect(Collectors.toList());

            // Add to the DTO list
            HubFullViewDTO hubDTO = new HubFullViewDTO(hub.getId(), hub.getState(), hub.getCity(),
                    hub.getCountry(), hub.getAddress(), phone, workHour);
            hubFullViewList.add(hubDTO);
        }

        // Return the response with paginated data
        return ResponseEntity.ok(hubFullViewList);
    }

}
