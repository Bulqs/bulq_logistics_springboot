package com.bulq.logistics.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bulq.logistics.models.Account;
import com.bulq.logistics.models.Notification;
import com.bulq.logistics.payload.notification.AddNotificationPayloadDTO;
import com.bulq.logistics.payload.notification.NotificationViewDTO;
import com.bulq.logistics.services.AccountService;
import com.bulq.logistics.services.NotificationService;
import com.bulq.logistics.util.constants.ReadStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/notifications")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "Controller for Notifications")
public class NotificationController {

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final NotificationService notificationService;

    @PostMapping("/send-notification")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "201", description = "Notification sent")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Send a notification")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> addService(@Valid @RequestBody AddNotificationPayloadDTO payloadDTO) {
        Optional<Account> optionalAccount = accountService.findById(payloadDTO.getAccountId());
        Account account;

        if (!optionalAccount.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        account = optionalAccount.get();

        try {
            Notification notification = new Notification();
            notification.setAccount(account);
            notification.setImage(account.getImage());
            notification.setMessage(payloadDTO.getMessage());
            notification.setStatus(ReadStatus.UNREAD);
            notificationService.save(notification);

            return ResponseEntity.ok("Card added to wallet successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/{accountId}/view-notification", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "View notifications for a user")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "View single rating")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<List<NotificationViewDTO>> viewComplaint(@PathVariable("accountId") Long accountId,
            Authentication authentication) {
        String email = "";
        email = authentication.getName();
        Optional<Account> optionalAccountAuth = accountService.findByEmail(email);
        Account accountAuth;
        if (!optionalAccountAuth.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        Optional<Account> optionalAccount = accountService.findById(accountId);
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        Account account;
        accountAuth = optionalAccountAuth.get();
        account = optionalAccount.get();
        if (accountAuth.getId() != account.getId() || account.getAuthorities() == "ADMIN") {
            return ResponseEntity.badRequest().body(null);
        }

        List<NotificationViewDTO> notifications = new ArrayList<>();
        for (Notification notification : notificationService.findByAccount_id(accountId)) {
            notifications.add(new NotificationViewDTO(notification.getId(), notification.getImage(),
                    notification.getMessage(), notification.getStatus()));
        }
        return ResponseEntity.ok(notifications);
    }

    @PutMapping(value = "/{notificationId}/update-notification", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "View notifications for a user")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "View single rating")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> updateNotification(@PathVariable("notificationId") Long notificationId,
            Authentication authentication) {
        String email = "";
        email = authentication.getName();
        Optional<Account> optionalAccountAuth = accountService.findByEmail(email);
        Optional<Notification> optionalNotification = notificationService.findById(notificationId);
        Account accountAuth;
        if (!optionalAccountAuth.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (!optionalNotification.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        Account account;
        Notification notification;
        accountAuth = optionalAccountAuth.get();
        notification = optionalNotification.get();
        if (accountAuth.getId() != notification.getAccount().getId() || accountAuth.getAuthorities() == "ADMIN") {
            return ResponseEntity.badRequest().body(null);
        }
        notification.setStatus(ReadStatus.READ);
        notification.setAccount(accountAuth);
        notificationService.save(notification);
        return ResponseEntity.ok("Notification has been read successfully!");
    }

}
