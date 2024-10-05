package com.bulq.logistics.controller;

import java.math.BigDecimal;
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
import com.bulq.logistics.models.Booking;
import com.bulq.logistics.models.Card;
import com.bulq.logistics.models.Transaction;
import com.bulq.logistics.models.Wallet;
import com.bulq.logistics.payload.card.CardViewDTO;
import com.bulq.logistics.payload.transaction.TransactionViewDTO;
import com.bulq.logistics.payload.wallet.AdminWalletViewDTO;
import com.bulq.logistics.payload.wallet.CreateWalletPayloadDTO;
import com.bulq.logistics.payload.wallet.DeductWalletPayloadDTO;
import com.bulq.logistics.payload.wallet.FundWalletPayloadDTO;
import com.bulq.logistics.payload.wallet.WalletAndCardViewDTO;
import com.bulq.logistics.payload.wallet.WalletViewDTO;
import com.bulq.logistics.services.AccountService;
import com.bulq.logistics.services.BookingService;
import com.bulq.logistics.services.CardService;
import com.bulq.logistics.services.TransactionService;
import com.bulq.logistics.services.WalletService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/wallet")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Wallet Controller", description = "Controller for Wallet management")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CardService cardService;

    @PostMapping("/create-wallet")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Issue with payload")
    @ApiResponse(responseCode = "201", description = "Wallet created")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new Wallet")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> createWallet(@Valid @RequestBody CreateWalletPayloadDTO payloadDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account;
        if (optionalAccount.isPresent()) {
            account = optionalAccount.get();
            Optional<Wallet> optionalWallet = walletService.findById(account.getId());

            try {
                if (optionalWallet.isPresent()) {
                    return ResponseEntity.badRequest().body("Your account already has a wallet chaperome");
                }
                Wallet wallet = new Wallet();
                wallet.setWalletName(payloadDTO.getName());
                wallet.setAccount(account);
                wallet.setCreatedAt(LocalDateTime.now());
                walletService.save(wallet);
                return ResponseEntity.ok("Wallet created successfully dear" + account.getFullName());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(null);
            }
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/fund-wallet")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Issue with payload")
    @ApiResponse(responseCode = "201", description = "Pickup a package added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new Wallet")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> fundWallet(@Valid @RequestBody FundWalletPayloadDTO payloadDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Optional<Wallet> optionalWallet = walletService.findById(payloadDTO.getWalletId());
        Wallet wallet;
        Account account;

        if (optionalAccount.isPresent() && optionalWallet.isPresent()) {
            wallet = optionalWallet.get();
            account = optionalAccount.get();
        } else {
            return ResponseEntity.badRequest().body(null);
        }
        if (account.getId() != wallet.getAccount().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        walletService.fundWallet(wallet.getId(), new BigDecimal(payloadDTO.getAmount()));
        return ResponseEntity.ok("Your wallet has been funded successfully with " + payloadDTO.getAmount());
    }

    @PostMapping("/deduct-wallet")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Issue with payload")
    @ApiResponse(responseCode = "201", description = "Deduct wallet")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Deduct from Wallet")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> deductWallet(@Valid @RequestBody DeductWalletPayloadDTO payloadDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Optional<Wallet> optionalWallet = walletService.findById(payloadDTO.getWalletId());
        Optional<Booking> optionalBooking = bookingService.findById(payloadDTO.getBookingId());
        Wallet wallet;
        Account account;
        Booking booking;

        if (optionalAccount.isPresent() && optionalWallet.isPresent()) {
            wallet = optionalWallet.get();
            account = optionalAccount.get();
            booking = optionalBooking.get();
        } else {
            return ResponseEntity.badRequest().body(null);
        }
        if (account.getId() != wallet.getAccount().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            walletService.deductFromWallet(wallet.getId(), booking, new BigDecimal(payloadDTO.getAmount()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid Account or Insufficient amount");
        }
        return ResponseEntity.ok("Your wallet has been funded successfully with " + payloadDTO.getAmount());
    }

    @GetMapping(value = "/{walletId}/info", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "Wallet found")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "Find a wallet")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<WalletAndCardViewDTO> findWallet(@PathVariable("walletId") Long walletId,
            Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Optional<Wallet> optionalWallet = walletService.findById(walletId);
        Wallet wallet;
        Account account;

        if (optionalAccount.isPresent() && optionalWallet.isPresent()) {
            wallet = optionalWallet.get();
            account = optionalAccount.get();
        } else {
            return ResponseEntity.badRequest().body(null);
        }
        if (account.getId() != wallet.getAccount().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<CardViewDTO> cards = new ArrayList<>();

        for (Card card : cardService.findByWallet_id(wallet.getId())) {
            cards.add(new CardViewDTO(card.getId(), card.getCard_number(), card.getExpiry_date(), card.getCvv()));
        }

        WalletAndCardViewDTO walletViewDTO = new WalletAndCardViewDTO(wallet.getId(), wallet.getWalletName(),
                wallet.getBalance(), cards);
        return ResponseEntity.ok(walletViewDTO);
    }

    @GetMapping(value = "/{walletId}/info-transactions", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "Wallet found")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "Find a wallet")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<AdminWalletViewDTO> findWalletwithTransactions(@PathVariable("walletId") Long walletId) {
        Optional<Wallet> optionalWallet = walletService.findById(walletId);
        Wallet wallet;

        if (!optionalWallet.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        wallet = optionalWallet.get();

        List<TransactionViewDTO> transactions = new ArrayList<>();

        for (Transaction transaction : transactionService.findAllByWalletId(wallet.getId())) {
            transactions.add(new TransactionViewDTO(transaction.getId(), transaction.getTransactionDate(),
                    transaction.getTransactionType().toString(), transaction.getAmount(), transaction.getRecipient(),
                    transaction.getTransactionStatus().toString()));
        }

        AdminWalletViewDTO adminWalletViewDTO = new AdminWalletViewDTO(wallet.getId(), wallet.getWalletName(),
                wallet.getBalance(), transactions);

        return ResponseEntity.ok(adminWalletViewDTO);
    }

    @GetMapping("/all-wallets")
    @ApiResponse(responseCode = "200", description = "List of users pagination")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List users in paginated format")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<List<WalletViewDTO>> allWallets(
            @RequestParam(required = false, name = "createdAt", defaultValue = "createdAt") String sort_by,
            @RequestParam(required = false, name = "per_page", defaultValue = "2") String per_page,
            @RequestParam(required = false, name = "page", defaultValue = "1") String page,
            @RequestParam(required = false, name = "id", defaultValue = "") Long walletId,
            @RequestParam(required = false, name = "name", defaultValue = "") String walletName) {
        Page<Wallet> walletsOnPage = walletService.findByWalletInfo(Integer.parseInt(page) - 1,
                Integer.parseInt(per_page), sort_by, walletName, walletId);
        List<Wallet> walletList = walletsOnPage.getContent();
        int totalPages = walletsOnPage.getTotalPages();
        List<Integer> pages = new ArrayList<>();
        if (totalPages > 0) {
            pages = IntStream.rangeClosed(0, totalPages - 1).boxed().collect(Collectors.toList());
        }
        for (int link : pages) {
            // String active = "";
            if (link == walletsOnPage.getNumber()) {
                // active = "active";
            }
            if (pages != null) {
                List<WalletViewDTO> wallets = walletList.stream()
                        .map(wallet -> new WalletViewDTO(wallet.getId(), wallet.getWalletName(), wallet.getBalance()))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(wallets);
            }
        }
        return null;
    }

}
