package com.bulq.logistics.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bulq.logistics.models.Account;
import com.bulq.logistics.models.Booking;
import com.bulq.logistics.models.DeliveryActions;
import com.bulq.logistics.models.Notification;
import com.bulq.logistics.payload.deliveryactions.DeliveryActionPayloadDTO;
import com.bulq.logistics.services.AccountService;
import com.bulq.logistics.services.BookingService;
import com.bulq.logistics.services.DeliveryActionsService;
import com.bulq.logistics.services.EmailService;
import com.bulq.logistics.services.NotificationService;
import com.bulq.logistics.util.constants.EmailError;
import com.bulq.logistics.util.constants.ReadStatus;
import com.bulq.logistics.util.email.EmailDetailsWelcome;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/delivery-actions")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Delivery Actions Controller", description = "Controller for Delivery Actions")
public class DeliveryActionsController {

    @Autowired
    private DeliveryActionsService actionsService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "/{deliveryCode}/add-action", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please add valid name a description")
    @ApiResponse(responseCode = "204", description = "Album updated")
    @Operation(summary = "Update an Album")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> updateAlbum(@Valid @RequestBody DeliveryActionPayloadDTO payloadDTO,
            @PathVariable("deliveryCode") String deliveryCode,
            Authentication authentication) {
        String email = authentication.getName();
        Account account;
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Optional<Booking> optionalBooking = bookingService.findByDeliveryId(deliveryCode);
        Booking booking;
        account = optionalAccount.get();

        if (optionalBooking.isPresent()) {
            booking = optionalBooking.get();
        } else {
            return ResponseEntity.badRequest().body(null);
        }

        if (account.getId() != optionalBooking.get().getAccount().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        try {
            DeliveryActions actions = new DeliveryActions();
            actions.setActivity(payloadDTO.getActivity());
            actions.setCreatedAt(null);
            actions.setBooking(booking);
            String subject = "Booking success";
            EmailDetailsWelcome receiverDetails = new EmailDetailsWelcome(booking.getReceiver_email(),
                    payloadDTO.getActivity(), subject,
                    booking.getReceiver_firstname());
            if (emailService.sendDeliveryActionsMail(receiverDetails) == false) {
                log.debug(EmailError.Email_Error.toString() + " " + "Error sending mail");
                return ResponseEntity.badRequest().body("Error sending mail, contact admin");
            }
            ;
            actionsService.save(actions);
            Notification notification = new Notification();
            notification.setAccount(account);
            notification.setImage(account.getImage());
            notification.setMessage(actions.getActivity());
            notification.setStatus(ReadStatus.UNREAD);
            notificationService.save(notification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(payloadDTO.getActivity());
    }
}
