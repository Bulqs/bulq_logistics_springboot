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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bulq.logistics.models.Account;
import com.bulq.logistics.models.Rating;
import com.bulq.logistics.payload.rating.AddRatingPayloadDTO;
import com.bulq.logistics.payload.rating.RatingViewDTO;
import com.bulq.logistics.services.AccountService;
import com.bulq.logistics.services.RatingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/ratings")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Rating Controller", description = "Controller for Rating")
public class RatingController {

    @Autowired
    private final RatingService ratingService;

    @Autowired
    private final AccountService accountService;

    @PostMapping("/rate")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please pass the correct payload")
    @ApiResponse(responseCode = "201", description = "Complaint added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new Rating")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<RatingViewDTO> addRating(@Valid @RequestBody AddRatingPayloadDTO payloadDTO,
            Authentication authentication) {
        String email = "";
        email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account;
        if (!optionalAccount.isPresent()) {
            ResponseEntity.badRequest().body(null);
        }
        account = optionalAccount.get();
        try {
            Rating rating = new Rating();
            rating.setComment(payloadDTO.getComment());
            rating.setStars(payloadDTO.getStars());
            rating.setUserImage(account.getImage());
            rating.setUsername(account.getUsername());
            rating.setCreatedAt(LocalDateTime.now());
            // rating.setAccount(account);
            ratingService.save(rating);

            RatingViewDTO ratingViewDTO = new RatingViewDTO(rating.getId(), rating.getUsername(), rating.getUserImage(),
                    rating.getComment(), rating.getStars());

            return ResponseEntity.ok(ratingViewDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/")
    @ApiResponse(responseCode = "200", description = "List of ratings pagination")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List ratings in paginated format")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<List<RatingViewDTO>> allRatings(
            @RequestParam(required = false, name = "createdAt", defaultValue = "createdAt") String sort_by,
            @RequestParam(required = false, name = "per_page", defaultValue = "2") String per_page,
            @RequestParam(required = false, name = "page", defaultValue = "1") String page,
            @RequestParam(required = false, name = "id", defaultValue = "1") Long id,
            @RequestParam(required = false, name = "username", defaultValue = "RedOx98") String username,
            @RequestParam(required = false, name = "stars", defaultValue = "") String stars // Removed comma
    ) {
        Page<Rating> ratingsOnPage = ratingService.findByRatingInfo(
                Integer.parseInt(page) - 1,
                Integer.parseInt(per_page),
                sort_by,
                id,
                username,
                stars);

        List<Rating> ratingList = ratingsOnPage.getContent();

        // Total pages calculation
        int totalPages = ratingsOnPage.getTotalPages();
        List<Integer> pages = new ArrayList<>();

        if (totalPages > 0) {
            pages = IntStream.rangeClosed(0, totalPages - 1).boxed().collect(Collectors.toList());
        }

        // Constructing the response
        if (!ratingList.isEmpty()) {
            List<RatingViewDTO> ratings = ratingList.stream().map(rating -> new RatingViewDTO(
                    rating.getId(),
                    rating.getUsername(),
                    rating.getUserImage(),
                    rating.getComment(),
                    rating.getStars())).collect(Collectors.toList());

            return ResponseEntity.ok(ratings);
        }

        // Return empty list if no content
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping(value = "/{ratingId}/view-rating", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "View single rating")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "View single rating")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<RatingViewDTO> viewComplaint(@PathVariable("ratingId") Long ratingId) {
        Optional<Rating> optionalRating = ratingService.findById(ratingId);
        if (optionalRating.isPresent()) {
            Rating rating = optionalRating.get();
            ratingService.findById(ratingId);

            RatingViewDTO ratingViewDTO = new RatingViewDTO(rating.getId(), rating.getUsername(), rating.getUserImage(),
                    rating.getComment(), rating.getStars());
            System.out.println(ratingViewDTO);
            return ResponseEntity.ok(ratingViewDTO);

        }
        return ResponseEntity.badRequest().body(null);
    }

}
