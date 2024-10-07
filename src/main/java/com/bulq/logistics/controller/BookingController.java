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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bulq.logistics.models.Account;
import com.bulq.logistics.models.Booking;
import com.bulq.logistics.models.DeliveryActions;
import com.bulq.logistics.models.Notification;
import com.bulq.logistics.payload.booking.BADOBookingPayloadDTO;
import com.bulq.logistics.payload.booking.BookingSummaryDTO;
import com.bulq.logistics.payload.booking.UpdateBookingPayloadDTO;
import com.bulq.logistics.payload.booking.DPBookingPayloadDTO;
import com.bulq.logistics.payload.booking.FilterBookingViewDTO;
import com.bulq.logistics.payload.booking.PUPBookingPayloadDTO;
import com.bulq.logistics.payload.booking.TrackingBookingViewDTO;
import com.bulq.logistics.payload.deliveryactions.DeliveryActionViewDTO;
import com.bulq.logistics.services.AccountService;
import com.bulq.logistics.services.BookingService;
import com.bulq.logistics.services.DeliveryActionsService;
import com.bulq.logistics.services.EmailService;
import com.bulq.logistics.services.NotificationService;
import com.bulq.logistics.util.AppUtil;
import com.bulq.logistics.util.constants.EmailError;
import com.bulq.logistics.util.constants.PickupType;
import com.bulq.logistics.util.constants.ReadStatus;
import com.bulq.logistics.util.constants.ShipmentType;
import com.bulq.logistics.util.email.EmailDetailsWelcome;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.bulq.logistics.util.constants.Status;

@RestController
@RequestMapping("/api/v1/booking")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Booking Controller", description = "Controller for Account management")
public class BookingController {

    @Autowired
    private final AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private DeliveryActionsService actionsService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/pup")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Issue with payload")
    @ApiResponse(responseCode = "201", description = "Pickup a package added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new Pick up package service")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> bookPup(@Valid @RequestBody PUPBookingPayloadDTO payloadDTO,
            Authentication authentication) {
        System.out.println(payloadDTO.toString());
        System.out.println(payloadDTO.getSender_firstname());
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        System.out.println(optionalAccount);
        if (optionalAccount.isPresent()) {
            try {
                Account account = optionalAccount.get();
                Booking booking = new Booking();
                booking.setSender_firstname(payloadDTO.getSender_firstname());
                System.out.println(payloadDTO.getSender_firstname());
                booking.setSender_lastname(payloadDTO.getSender_lastname());
                booking.setSender_address(payloadDTO.getSender_address());
                booking.setSender_email(payloadDTO.getSender_email());
                booking.setSender_country(payloadDTO.getSender_country());
                booking.setSender_city(payloadDTO.getSender_city());
                booking.setSender_state(payloadDTO.getSender_state());
                booking.setSender_lga(payloadDTO.getSender_lga());
                booking.setCreatedAt(LocalDateTime.now());
                booking.setSender_phoneNumber(payloadDTO.getSender_phoneNumber());
                booking.setReceiver_firstname(payloadDTO.getReceiver_firstname());
                booking.setReceiver_lastname(payloadDTO.getReceiver_lastname());
                booking.setReceiver_address(payloadDTO.getReceiver_address());
                booking.setReceiver_email(payloadDTO.getReceiver_email());
                booking.setReceiver_country(payloadDTO.getReceiver_country());
                booking.setReceiver_city(payloadDTO.getReceiver_city());
                booking.setReceiver_state(payloadDTO.getReceiver_state());
                booking.setReceiver_lga(payloadDTO.getReceiver_lga());
                booking.setReceiver_phoneNumber(payloadDTO.getReceiver_phoneNumber());
                booking.setPackage_name(payloadDTO.getPackage_name());
                booking.setPackage_description(payloadDTO.getPackage_description());
                booking.setPackage_image(payloadDTO.getPackage_image());
                booking.setPick_up_date(payloadDTO.getPick_up_date());
                booking.setPick_up_time(payloadDTO.getPick_up_time());
                booking.setPickupType(PickupType.fromValue(payloadDTO.getPickupType().toString()));
                booking.setShipment_type(ShipmentType.fromValue(payloadDTO.getShipment_type().toString()));
                booking.setAccount(account);
                AppUtil util = new AppUtil();
                String deliveryId = util.stringGenerator(booking.getId());
                booking.setDeliveryId(deliveryId);
                booking.setShipping_amount(payloadDTO.getShipping_amount());
                bookingService.save(booking);

                String subject = "Booking success";
                EmailDetailsWelcome senderDetails = new EmailDetailsWelcome(account.getEmail(), deliveryId, subject,
                        payloadDTO.getSender_firstname());
                System.out.println(senderDetails);
                EmailDetailsWelcome receiverDetails = new EmailDetailsWelcome(payloadDTO.getReceiver_email(),
                        deliveryId, subject,
                        payloadDTO.getSender_firstname());
                System.out.println(receiverDetails);
                if ((emailService.sendLogisticsIdMail(senderDetails) == false)
                        && (emailService.sendLogisticsIdMail(receiverDetails) == false)) {
                    log.debug(EmailError.Email_Error.toString() + " " + "Error sending mail");
                    return ResponseEntity.badRequest().body("Error sending mail, contact admin");
                }
                ;
                Notification notification = new Notification();
                notification.setAccount(account);
                notification.setImage(booking.getPackage_image());
                String message = "Your delivery code for package ";
                message += booking.getPackage_name();
                message += " " + booking.getDeliveryId();
                notification.setMessage(message);
                notification.setStatus(ReadStatus.UNREAD);
                notificationService.save(notification);

                return ResponseEntity.ok("Your delivery code is as follows: " + deliveryId);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/dp")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Issue with payload")
    @ApiResponse(responseCode = "201", description = "Pickup a package added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new Deliver package service")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> bookDp(@Valid @RequestBody DPBookingPayloadDTO payloadDTO,
            Authentication authentication) {
        System.out.println(payloadDTO.toString());
        System.out.println(payloadDTO.getSender_firstname());
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        System.out.println(optionalAccount);
        if (optionalAccount.isPresent()) {
            try {
                Account account = optionalAccount.get();
                Booking booking = new Booking();
                booking.setSender_firstname(payloadDTO.getSender_firstname());
                System.out.println(payloadDTO.getSender_firstname());
                booking.setSender_lastname(payloadDTO.getSender_lastname());
                booking.setSender_address(payloadDTO.getSender_address());
                booking.setSender_email(payloadDTO.getSender_email());
                booking.setSender_country(payloadDTO.getSender_country());
                booking.setSender_city(payloadDTO.getSender_city());
                booking.setSender_state(payloadDTO.getSender_state());
                booking.setSender_lga(payloadDTO.getSender_lga());
                booking.setSender_phoneNumber(payloadDTO.getSender_phoneNumber());
                booking.setReceiver_firstname(payloadDTO.getReceiver_firstname());
                booking.setReceiver_lastname(payloadDTO.getReceiver_lastname());
                booking.setCreatedAt(LocalDateTime.now());
                booking.setReceiver_address(payloadDTO.getReceiver_address());
                booking.setReceiver_email(payloadDTO.getReceiver_email());
                booking.setReceiver_country(payloadDTO.getReceiver_country());
                booking.setReceiver_city(payloadDTO.getReceiver_city());
                booking.setReceiver_state(payloadDTO.getReceiver_state());
                booking.setReceiver_lga(payloadDTO.getReceiver_lga());
                booking.setReceiver_phoneNumber(payloadDTO.getReceiver_phoneNumber());
                booking.setPackage_name(payloadDTO.getPackage_name());
                booking.setPackage_description(payloadDTO.getPackage_description());
                booking.setPackage_image(payloadDTO.getPackage_image());
                booking.setPick_up_date(payloadDTO.getPick_up_date());
                booking.setPick_up_time(payloadDTO.getPick_up_time());
                booking.setPickupType(PickupType.fromValue(payloadDTO.getPickupType().toString()));
                booking.setShipment_type(ShipmentType.fromValue(payloadDTO.getShipment_type().toString()));
                booking.setAccount(account);
                AppUtil util = new AppUtil();
                String deliveryId = util.stringGenerator(booking.getId());
                booking.setDeliveryId(deliveryId);
                booking.setShipping_amount(payloadDTO.getShipping_amount());
                bookingService.save(booking);

                String subject = "Booking success";
                EmailDetailsWelcome senderDetails = new EmailDetailsWelcome(account.getEmail(), deliveryId, subject,
                        payloadDTO.getSender_firstname());
                System.out.println(senderDetails);
                EmailDetailsWelcome receiverDetails = new EmailDetailsWelcome(payloadDTO.getReceiver_email(),
                        deliveryId, subject,
                        payloadDTO.getSender_firstname());
                System.out.println(receiverDetails);
                if ((emailService.sendLogisticsIdMail(senderDetails) == false)
                        && (emailService.sendLogisticsIdMail(receiverDetails) == false)) {
                    log.debug(EmailError.Email_Error.toString() + " " + "Error sending mail");
                    return ResponseEntity.badRequest().body("Error sending mail, contact admin");
                }
                ;

                Notification notification = new Notification();
                notification.setAccount(account);
                notification.setImage(booking.getPackage_image());
                String message = "Your delivery code for package ";
                message += booking.getPackage_name();
                message += " " + booking.getDeliveryId();
                notification.setMessage(message);
                notification.setStatus(ReadStatus.UNREAD);
                notificationService.save(notification);

                return ResponseEntity.ok("Your delivery code is as follows: " + deliveryId);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/bado")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Issue with payload")
    @ApiResponse(responseCode = "201", description = "Book a drop off package added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new Deliver package service")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> bookBADO(@Valid @RequestBody BADOBookingPayloadDTO payloadDTO,
            Authentication authentication) {
        System.out.println(payloadDTO.toString());
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        System.out.println(optionalAccount);
        if (optionalAccount.isPresent()) {
            try {
                Account account = optionalAccount.get();
                Booking booking = new Booking();

                booking.setCity(payloadDTO.getCity());
                booking.setCountry(payloadDTO.getCountry());
                booking.setReceiver_state(payloadDTO.getState());
                booking.setLga(payloadDTO.getLga());
                booking.setAddress(payloadDTO.getAddress());
                booking.setPackage_name(payloadDTO.getPackage_name());
                booking.setPackage_description(payloadDTO.getPackage_description());
                booking.setPackage_image(payloadDTO.getPackage_image());
                booking.setPick_up_date(payloadDTO.getPick_up_date());
                booking.setPick_up_time(payloadDTO.getPick_up_time());
                booking.setPickupType(PickupType.fromValue(payloadDTO.getPickupType().toString()));
                booking.setShipment_type(ShipmentType.fromValue(payloadDTO.getShipment_type().toString()));
                booking.setCreatedAt(LocalDateTime.now());
                booking.setAccount(account);
                AppUtil util = new AppUtil();
                String deliveryId = util.stringGenerator(booking.getId());
                booking.setDeliveryId(deliveryId);
                booking.setShipping_amount(payloadDTO.getShipping_amount());
                bookingService.save(booking);

                String subject = "Booking success";
                EmailDetailsWelcome senderDetails = new EmailDetailsWelcome(account.getEmail(), deliveryId, subject,
                        payloadDTO.getEmail());
                if ((emailService.sendLogisticsIdMail(senderDetails) == false)) {
                    log.debug(EmailError.Email_Error.toString() + " " + "Error sending mail");
                    return ResponseEntity.badRequest().body("Error sending mail, contact admin");
                }
                ;

                Notification notification = new Notification();
                notification.setAccount(account);
                notification.setImage(booking.getPackage_image());
                String message = "Your delivery code for package ";
                message += booking.getPackage_name();
                message += " " + booking.getDeliveryId();
                notification.setMessage(message);
                notification.setStatus(ReadStatus.UNREAD);
                notificationService.save(notification);

                return ResponseEntity.ok("Your delivery code is as follows: " + deliveryId);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/")
    @ApiResponse(responseCode = "200", description = "List of users pagination")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List users in paginated format")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<List<FilterBookingViewDTO>> allUsers(
            @RequestParam(required = false, name = "createdAt", defaultValue = "delivery_status") String sort_by,
            @RequestParam(required = false, name = "per_page", defaultValue = "2") String per_page,
            @RequestParam(required = false, name = "page", defaultValue = "1") String page,
            @RequestParam(required = false, name = "sender_firstName", defaultValue = "") String sender,
            @RequestParam(required = false, name = "receiver_firstName", defaultValue = "") String receiver,
            @RequestParam(required = false, name = "deliveryId", defaultValue = "qwe345") String deliveryId,
            @RequestParam(required = false, name = "shipment_type", defaultValue = "") ShipmentType shipment_type,
            @RequestParam(required = false, name = "pickupType,", defaultValue = "") PickupType pickupType,
            @RequestParam(required = false, name = "phoneNumber,", defaultValue = "") String phoneNumber,
            @RequestParam(required = false, name = "email", defaultValue = "") String email) {
        Page<Booking> bookingsOnPage = bookingService.findByBookingInfo(Integer.parseInt(page) - 1,
                Integer.parseInt(per_page), sort_by, sender, receiver, deliveryId, shipment_type, pickupType,
                phoneNumber, email);
        List<Booking> bookingList = bookingsOnPage.getContent();
        int totalPages = bookingsOnPage.getTotalPages();
        List<Integer> pages = new ArrayList<>();
        if (totalPages > 0) {
            pages = IntStream.rangeClosed(0, totalPages - 1).boxed().collect(Collectors.toList());
        }
        for (int link : pages) {
            // String active = "";
            if (link == bookingsOnPage.getNumber()) {
                // active = "active";
            }
            if (pages != null) {
                List<FilterBookingViewDTO> bookings = bookingList.stream()
                        .map(booking -> new FilterBookingViewDTO(booking.getId(), booking.getDeliveryId(),
                                booking.getSender_lastname(), booking.getSender_phoneNumber(),
                                booking.getReceiver_phoneNumber(), booking.getReceiver_address(),
                                booking.getReceiver_email(), booking.getPhoneNumber(), booking.getAddress(),
                                booking.getEmail(), booking.getCity(), booking.getCountry(), booking.getLga(),
                                booking.getPackage_name(), booking.getWeight(), booking.getPackage_description(),
                                booking.getShipment_type().toString(), booking.getPickupType().toString(),
                                booking.getShipping_amount(), booking.getPick_up_date(), booking.getPick_up_time(),
                                booking.getDropoff_date(), booking.getDropoff_time()))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(bookings);
            }
        }
        return null;
    }

    @GetMapping(value = "/{deliveryCode}", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "delivery code found success")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "Track a booking")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<TrackingBookingViewDTO> trackOrder(@PathVariable("deliveryCode") String deliveryId,
            Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();
        Optional<Booking> optionalBooking = bookingService.findByDeliveryId(deliveryId);
        Booking booking;
        if (optionalBooking.isPresent()) {
            booking = optionalBooking.get();
        } else {
            return ResponseEntity.badRequest().body(null);
        }
        if (account.getId() != booking.getAccount().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<DeliveryActionViewDTO> actions = new ArrayList<>();

        for (DeliveryActions action : actionsService.findByDeliveryId(deliveryId.toString())) {
            // new DeliveryActionViewDTO(action.getId()
            // , action.getActivity());
            actions.add(new DeliveryActionViewDTO(action.getId(), action.getActivity()));
        }

        System.out.println();
        TrackingBookingViewDTO bookingViewDTO = new TrackingBookingViewDTO(booking.getId(), booking.getDeliveryId(),
                booking.getSender_lastname(), booking.getSender_phoneNumber(), booking.getReceiver_phoneNumber(),
                booking.getReceiver_address(), booking.getReceiver_email(), booking.getPhoneNumber(),
                booking.getAddress(), booking.getEmail(), booking.getCity(), booking.getCountry(), booking.getLga(),
                booking.getPackage_name(), booking.getWeight(), booking.getPackage_description(),
                booking.getShipment_type().toString(), booking.getPickupType().toString(), booking.getShipping_amount(),
                booking.getPick_up_date(), booking.getPick_up_time(), booking.getDropoff_date(),
                booking.getDropoff_time(), actions);

        return ResponseEntity.ok(bookingViewDTO);
    }

    @PutMapping(value = "/booking/{deliveryCode}/update", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please add valid name a description")
    @ApiResponse(responseCode = "204", description = "Album updated")
    @Operation(summary = "Update an Album")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> updateAlbum(@Valid @RequestBody UpdateBookingPayloadDTO payloadDTO,
            @PathVariable("deliveryCode") String deliveryCode,
            Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Optional<Booking> optionalBooking = bookingService.findByDeliveryId(deliveryCode);
        Account account;
        if (optionalBooking.isPresent()) {
            account = optionalAccount.get();
        } else {
            return ResponseEntity.badRequest().body(null);
        }
        if (account.getId() != optionalBooking.get().getAccount().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Booking booking = optionalBooking.get();
        booking.setDelivery_status(payloadDTO.getCancel());
        bookingService.save(booking);
        return ResponseEntity.ok("Order cancelled successfully");
    }

    @GetMapping("/summary")
    @ApiResponse(responseCode = "200", description = "booking summary stats")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "get summary of bookings")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<List<BookingSummaryDTO>> getBookingSummary(
            @RequestParam(required = false) List<Status> statuses,
            @RequestParam(required = false) Integer day,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) Integer year) {

        List<BookingSummaryDTO> bookingSummary = bookingService.getBookingSummary(statuses, day, month, year);
        return new ResponseEntity<>(bookingSummary, HttpStatus.OK);
    }

    @GetMapping("/summary-amounts")
    @ApiResponse(responseCode = "200", description = "booking summary with amount stats")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "get summary of bookings with amounts earned")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<List<BookingSummaryDTO>> getBookingSummaryWithAmount(
            @RequestParam(required = false) List<Status> statuses,
            @RequestParam(required = false) Integer day,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) Integer year) {

        List<BookingSummaryDTO> bookingSummary = bookingService.getBookingSummary(statuses, day, month, year);
        return new ResponseEntity<>(bookingSummary, HttpStatus.OK);
    }
}
