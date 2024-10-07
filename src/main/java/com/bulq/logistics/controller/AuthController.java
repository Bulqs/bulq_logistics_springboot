package com.bulq.logistics.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

import com.bulq.logistics.models.Account;
import com.bulq.logistics.models.Booking;
import com.bulq.logistics.payload.auth.AccountDTO;
import com.bulq.logistics.payload.auth.AccountVerifiedViewDTO;
import com.bulq.logistics.payload.auth.AccountViewDTO;
import com.bulq.logistics.payload.auth.AuthoritiesDTO;
import com.bulq.logistics.payload.auth.ChangePasswordPayloadDTO;
import com.bulq.logistics.payload.auth.PasswordDTO;
import com.bulq.logistics.payload.auth.PasswordResetPayloadDTO;
import com.bulq.logistics.payload.auth.ProfileViewDTO;
import com.bulq.logistics.payload.auth.TokenDTO;
import com.bulq.logistics.payload.auth.UpdateAccountPayloadDTO;
import com.bulq.logistics.payload.auth.UpdateAccountViewDTO;
import com.bulq.logistics.payload.auth.UserLoginDTO;
import com.bulq.logistics.payload.auth.VerificationDTO;
import com.bulq.logistics.payload.booking.GeneralBookingViewDTO;
import com.bulq.logistics.services.AccountService;
import com.bulq.logistics.services.BookingService;
import com.bulq.logistics.services.EmailService;
import com.bulq.logistics.services.TokenService;
import com.bulq.logistics.util.auth.CustomUserDetails;
import com.bulq.logistics.util.constants.AccountError;
import com.bulq.logistics.util.constants.AccountSuccess;
import com.bulq.logistics.util.constants.EmailError;
import com.bulq.logistics.util.constants.Verification;
import com.bulq.logistics.util.email.EmailDetailsWelcome;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Controller for Account management")
public class AuthController {

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final TokenService tokenService;

    @Autowired
    private final AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BookingService bookingService;

    @Value("${password.token.reset..timeout.minutes}")
    private int passwordTokenTimeout;

    @Value("${site.domain}")
    private String siteDomain;

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenDTO> token(@Valid @RequestBody UserLoginDTO userLogin) throws AuthenticationException {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword()));
            // Extract user details from authentication object
            CustomUserDetails userDetails;
            userDetails = (CustomUserDetails) authentication.getPrincipal(); // assuming is the principal class
            String token = tokenService.generateToken(authentication);

            return ResponseEntity.ok(new TokenDTO(token, userDetails.getFirstName(), userDetails.getAuthorities()));
        } catch (Exception e) {
            log.debug(AccountError.TOKEN_GENERATION_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/users/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "201", description = "Account added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new user")
    public ResponseEntity<String> register(@Valid @RequestBody AccountDTO accountDTO) throws AuthenticationException {
        try {
            Account account = new Account();
            account.setEmail(accountDTO.getEmail());
            account.setPassword(accountDTO.getPassword());
            account.setFirstName(accountDTO.getFirstName());
            account.setLastName(accountDTO.getLastName());
            account.setCountry(accountDTO.getCountry());
            account.setState(accountDTO.getState());
            account.setCity(accountDTO.getCity());
            account.setCreatedAt(LocalDateTime.now());
            account.setAddress(accountDTO.getAddress());
            account.setPhoneNumber(accountDTO.getPhoneNumber());
            String verificationToken = UUID.randomUUID().toString();
            // String otp = RandomStringUtils.randomNumeric(length);
            String verificationLink = "This is the verfication link: " + siteDomain
                    + "api/v1/auth/users/verification-lnk?token="
                    + verificationToken;
            account.setOtp(verificationToken);
            account.setPassswordResetTokenExpiry(LocalDateTime.now().plusMinutes(passwordTokenTimeout));
            accountService.save(account);
            String subject = "Bulq just testing";
            EmailDetailsWelcome details = new EmailDetailsWelcome(accountDTO.getEmail(), verificationLink, subject,
                    accountDTO.getFirstName());
            System.out.println(details);
            if (emailService.sendWelcomeEmail(details) == false) {
                log.debug(EmailError.Email_Error.toString() + " " + "Error sending mail");
                return new ResponseEntity<>("Error while sending mail, contact admin", HttpStatus.EXPECTATION_FAILED);
            }
            ;
            return ResponseEntity.ok(AccountSuccess.ACCOUNT_ADDED.toString());
        } catch (Exception e) {
            log.debug(AccountError.ADD_ACCOUNT_ERROR.toString() + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/users/verification-lnk")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "201", description = "Account added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new user")
    public ResponseEntity<AccountVerifiedViewDTO> verify(
            @Valid @RequestParam("token") VerificationDTO verificationDTO) {
        Optional<Account> optionalAccount = accountService.findByOtp(verificationDTO.getVerificationToken());
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Account account = optionalAccount.get();
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(account.getPassswordResetTokenExpiry())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        account.setVerified(Verification.VERIFIED);
        account.setOtp("");
        accountService.save(account);
        AccountVerifiedViewDTO verifiedDTO = new AccountVerifiedViewDTO(account.getId(), account.getEmail(),
                account.getAuthorities(), account.getCreatedAt(), account.getPhoneNumber(),
                account.getVerified().toString());
        return ResponseEntity.ok(verifiedDTO);
    }

    @PutMapping("/user/profile/{userId}/update-profile")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Update user profile by admin")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<UpdateAccountViewDTO> updateProfile(@Valid @RequestBody UpdateAccountPayloadDTO accountDTO,
            @PathVariable Long userId, Authentication authentication) {
        Optional<Account> optionalAccount = accountService.findById(userId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setEmail(accountDTO.getEmail());
            account.setFirstName(accountDTO.getFirstName());
            account.setLastName(accountDTO.getLastName());
            account.setCountry(accountDTO.getCountry());
            account.setCity(accountDTO.getCity());
            account.setState(accountDTO.getState());
            account.setAddress(accountDTO.getState());
            account.setPhoneNumber(accountDTO.getPhoneNumber());
            accountService.save(account);

            UpdateAccountViewDTO accountViewDTO = new UpdateAccountViewDTO(
                    account.getEmail(),
                    account.getPhoneNumber(),
                    account.getFirstName(),
                    account.getLastName(),
                    account.getUsername(),
                    account.getCountry(),
                    account.getState(),
                    account.getCity(),
                    account.getAddress());
            return ResponseEntity.ok(accountViewDTO);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping(value = "/users/{userId}/update-authorities", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "Updated authorities")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @ApiResponse(responseCode = "400", description = "Invalid user")
    @Operation(summary = "Update authorities")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<AccountViewDTO> updateAuthorities(@Valid @RequestBody AuthoritiesDTO authoritiesDTO,
            @PathVariable Long userId) {
        Optional<Account> optionalAccount = accountService.findById(userId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setAuthorities(authoritiesDTO.getAuthorities());
            accountService.save(account);
            AccountViewDTO accountViewDTO = new AccountViewDTO(
                    account.getId(),
                    account.getEmail(),
                    account.getAuthorities(),
                    account.getCreatedAt(),
                    account.getPhoneNumber(),
                    account.getFirstName(),
                    account.getLastName(),
                    account.getUsername(),
                    account.getVerified());
            return ResponseEntity.ok(accountViewDTO);
        }
        return new ResponseEntity<>(new AccountViewDTO(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/profile/update-password", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "update password")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<AccountViewDTO> updatePassword(@Valid @RequestBody PasswordDTO passwordDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();
        account.setPassword(passwordDTO.getPassword());
        accountService.save(account);
        AccountViewDTO accountViewDTO = new AccountViewDTO(
                account.getId(),
                account.getEmail(),
                account.getAuthorities(),
                account.getCreatedAt(),
                account.getPhoneNumber(),
                account.getFirstName(),
                account.getLastName(),
                account.getUsername(),
                account.getVerified());
        return ResponseEntity.ok(accountViewDTO);
    }

    @PostMapping(value = "/users/reset-password", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email")
    @ApiResponse(responseCode = "201", description = "Email sent!")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Forgot password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetPayloadDTO payloadDTO)
            throws Exception {
        Optional<Account> optionalAccount = accountService.findByEmail(payloadDTO.getEmail());
        if (optionalAccount.isPresent()) {
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            String resetToken = UUID.randomUUID().toString();
            account.setOtp(resetToken);
            account.setPassswordResetTokenExpiry(LocalDateTime.now().plusMinutes(passwordTokenTimeout));
            accountService.save(account);
            String verificationLink = "This is the reset password link: " + siteDomain
                    + "api/v1/auth/users/change-password?token=" + resetToken;
            String subject = "Bulq just testing";
            EmailDetailsWelcome details = new EmailDetailsWelcome(account.getEmail(), verificationLink, subject,
                    account.getFirstName());
            System.out.println(details);
            if (emailService.sendForgetPasswordEmail(details) == false) {
                log.debug(EmailError.Email_Error.toString() + " " + "Error sending mail");
                return new ResponseEntity<>("Error while sending mail, contact admin", HttpStatus.EXPECTATION_FAILED);
            }
            ;
            return new ResponseEntity<>("Password reset email sent", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No user found with the email supplied!", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping(value = "/users/change-password", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "417", description = "Invalid token!")
    @ApiResponse(responseCode = "201", description = "password changed")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "User to reset password")
    public ResponseEntity<String> changePassword(@Valid @RequestParam("token") String token,
            @RequestBody ChangePasswordPayloadDTO payloadDTO) {
        Optional<Account> optionalAccount = accountService.findByOtp(token);
        if (optionalAccount.isPresent()) {
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(optionalAccount.get().getPassswordResetTokenExpiry())) {
                return new ResponseEntity<>("Token Expired!!", HttpStatus.EXPECTATION_FAILED);
            }
            account = optionalAccount.get();
            Account accountById = accountService.findById(optionalAccount.get().getId()).get();
            accountById.setPassword(payloadDTO.getPassword());
            accountById.setOtp("");
            accountService.save(accountById);
            return new ResponseEntity<>("Password reset successfully!!!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Token Expired!!", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping(value = "/profile", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "User view Profile")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<ProfileViewDTO> profile(Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            List<GeneralBookingViewDTO> bookings = new ArrayList<>();
            for (Booking booking : bookingService.findPendingBookingsByAccountId(account.getId())) {
                bookings.add(
                        new GeneralBookingViewDTO(booking.getId(), booking.getDeliveryId(),
                                booking.getSender_lastname(), booking.getSender_phoneNumber(),
                                booking.getReceiver_phoneNumber(), booking.getReceiver_address(),
                                booking.getReceiver_email(), booking.getPhoneNumber(), booking.getAddress(),
                                booking.getEmail(), booking.getCity(), booking.getCountry(), booking.getLga(),
                                booking.getPackage_name(), booking.getWeight(), booking.getPackage_description(),
                                booking.getShipment_type().toString(), booking.getPickupType().toString(),
                                booking.getShipping_amount(), booking.getPick_up_date(), booking.getPick_up_time(),
                                booking.getDropoff_date(), booking.getDropoff_time()));
            }
            ProfileViewDTO profileViewDTO = new ProfileViewDTO(account.getId(), account.getEmail(),
                    account.getAuthorities(), account.getCreatedAt(), account.getPhoneNumber(), account.getFirstName(),
                    account.getLastName(), account.getUsername(), account.getVerified().toString(), bookings);
            return ResponseEntity.ok(profileViewDTO);

        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/users")
    @ApiResponse(responseCode = "200", description = "List of users pagination")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List users in paginated format")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<List<AccountViewDTO>> allUsers(
            @RequestParam(required = false, name = "sort_by", defaultValue = "createdAt") String sort_by,
            @RequestParam(required = false, name = "per_page", defaultValue = "2") String per_page,
            @RequestParam(required = false, name = "page", defaultValue = "1") String page,
            @RequestParam(required = false, name = "name", defaultValue = "") String name,
            @RequestParam(required = false, name = "username", defaultValue = "") String username,
            @RequestParam(required = false, name = "phoneNumber", defaultValue = "pending") String authorities,
            @RequestParam(required = false, name = "authorities", defaultValue = "") String phoneNumber,
            @RequestParam(required = false, name = "email", defaultValue = "") String email) {
        Page<Account> accountsOnPage = accountService.findAccounts(Integer.parseInt(page) - 1,
                Integer.parseInt(per_page), sort_by, name, username, authorities, phoneNumber, email);
        List<Account> accountList = accountsOnPage.getContent();
        int totalPages = accountsOnPage.getTotalPages();
        List<Integer> pages = new ArrayList<>();
        if (totalPages > 0) {
            pages = IntStream.rangeClosed(0, totalPages - 1).boxed().collect(Collectors.toList());
        }
        for (int link : pages) {
            // String active = "";
            if (link == accountsOnPage.getNumber()) {
                // active = "active";
            }
            if (pages != null) {
                List<AccountViewDTO> accounts = accountList.stream().map(account -> new AccountViewDTO(
                        account.getId(),
                        account.getEmail(),
                        account.getAuthorities(),
                        account.getCreatedAt(),
                        account.getPhoneNumber(),
                        account.getFirstName(),
                        account.getLastName(),
                        account.getUsername(),
                        account.getVerified())).collect(Collectors.toList());
                return ResponseEntity.ok(accounts);
            }
        }
        return null;
    }

    @DeleteMapping(value = "/profile/delete")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Delete profile")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> deleteUser(Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        if (optionalAccount.isPresent()) {
            accountService.deleteById(optionalAccount.get().getId());
            return ResponseEntity.ok("User deleted successfully!");
        }
        return null;
    }

    @GetMapping("/home")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "bulq-demo-api")
    @Operation(summary = "welcome to bulq logistics")
    public ResponseEntity<String> bulq() {
        return ResponseEntity.ok("welcome to bulq logistics channel");
    }
}
