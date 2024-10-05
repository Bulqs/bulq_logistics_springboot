package com.bulq.logistics.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bulq.logistics.models.Complaint;
import com.bulq.logistics.payload.complaint.AddComplaintPayloadDTO;
import com.bulq.logistics.payload.complaint.ComplaintViewDTO;
import com.bulq.logistics.payload.complaint.UpdateComplaintPayloadDTO;
import com.bulq.logistics.services.ComplaintService;
import com.bulq.logistics.util.constants.ComplaintType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/complaints")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Complaint Controller", description = "Controller for Complaints")
public class ComplaintController {

    @Autowired
    private final ComplaintService complaintService;

    @PostMapping("/create-complaint")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please pass the correct payload")
    @ApiResponse(responseCode = "201", description = "Complaint added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new complaint")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> addComplaint(@Valid @RequestBody AddComplaintPayloadDTO payloadDTO) {
        try {
            Complaint complaint = new Complaint();
            complaint.setComplaint(payloadDTO.getComplaint());
            complaint.setDelivery_code(payloadDTO.getDelivery_code());
            complaint.setComplainant(payloadDTO.getComplainant());
            complaint.setCreatedAt(LocalDateTime.now());
            complaint.setComplaint_status(ComplaintType.OPEN);
            complaintService.save(complaint);

            return ResponseEntity.ok("Complaint Added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/")
    @ApiResponse(responseCode = "200", description = "List of complaints pagination")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List complaints in paginated format")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<List<ComplaintViewDTO>> allUsers(
            @RequestParam(required = false, name = "createdAt", defaultValue = "createdAt") String sort_by,
            @RequestParam(required = false, name = "per_page", defaultValue = "2") String per_page,
            @RequestParam(required = false, name = "page", defaultValue = "1") String page,
            @RequestParam(required = false, name = "id", defaultValue = "1") Long id,
            @RequestParam(required = false, name = "complaint", defaultValue = "") String complaint,
            @RequestParam(required = false, name = "complainant", defaultValue = "") String complainant,
            @RequestParam(required = false, name = "delivery_code", defaultValue = "") String delivery_code,
            @RequestParam(required = false, name = "complaint_status,", defaultValue = "") ComplaintType complaint_status) {
        Page<Complaint> complaintsOnPage = complaintService.findByComplaintInfo(Integer.parseInt(page) - 1,
                Integer.parseInt(per_page), sort_by, id, complaint, complainant, delivery_code, complaint_status);
        List<Complaint> complaintList = complaintsOnPage.getContent();
        int totalPages = complaintsOnPage.getTotalPages();
        List<Integer> pages = new ArrayList<>();
        if (totalPages > 0) {
            pages = IntStream.rangeClosed(0, totalPages - 1).boxed().collect(Collectors.toList());
        }
        for (int link : pages) {
            // String active = "";
            if (link == complaintsOnPage.getNumber()) {
                // active = "active";
            }
            if (pages != null) {
                List<ComplaintViewDTO> complaints = complaintList.stream()
                        .map(complain -> new ComplaintViewDTO(complain.getId(), complain.getComplaint(),
                                complain.getComplainant(), complain.getDelivery_code(),
                                complain.getComplaint_status().toString()))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(complaints);
            }
        }
        return null;
    }

    @PutMapping(value = "/update-complaint", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "List of catalogs")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "update complaint")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<ComplaintViewDTO> updateCatalog(@Valid @RequestBody UpdateComplaintPayloadDTO payloadDTO) {
        System.out.println(payloadDTO);
        Optional<Complaint> optionalComplaint = complaintService.findById(payloadDTO.getComplaintId());
        if (optionalComplaint.isPresent()) {
            Complaint complaint = optionalComplaint.get();
            complaint.setComplaint_status(payloadDTO.getComplaint_status());
            complaint.setResolution(payloadDTO.getResolution());
            complaintService.save(complaint);

            ComplaintViewDTO complaintViewDTO = new ComplaintViewDTO(complaint.getId(), complaint.getComplaint(),
                    complaint.getComplainant(), complaint.getDelivery_code(),
                    complaint.getComplaint_status().toString());
            return ResponseEntity.ok(complaintViewDTO);

        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping(value = "/{complaintId}/view-complaint", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "View single complaint")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "View single complaint")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<ComplaintViewDTO> viewComplaint(@PathVariable("complaintId") Long complaintId) {
        Optional<Complaint> optionalComplaint = complaintService.findById(complaintId);
        if (optionalComplaint.isPresent()) {
            Complaint complaint = optionalComplaint.get();
            complaintService.findById(complaintId);

            ComplaintViewDTO complaintViewDTO = new ComplaintViewDTO(complaint.getId(), complaint.getComplaint(),
                    complaint.getComplainant(), complaint.getDelivery_code(),
                    complaint.getComplaint_status().toString());
            return ResponseEntity.ok(complaintViewDTO);

        }
        return ResponseEntity.badRequest().body(null);
    }

}
