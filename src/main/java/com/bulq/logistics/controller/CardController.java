package com.bulq.logistics.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bulq.logistics.models.Account;
import com.bulq.logistics.models.Card;
import com.bulq.logistics.models.Wallet;
import com.bulq.logistics.payload.card.AddCardPayloadDTO;
import com.bulq.logistics.payload.card.DeleteCardPayloadDTO;
import com.bulq.logistics.services.AccountService;
import com.bulq.logistics.services.CardService;
import com.bulq.logistics.services.WalletService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/cards")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Card Controller", description = "Controller for Cards")
public class CardController {

    @Autowired
    private final CardService cardService;

    @Autowired
    private final WalletService walletService;

    @Autowired
    private final AccountService accountService;

    @PostMapping("/link-card")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "201", description = "Service Category added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Link card to your wallet")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> addService(@Valid @RequestBody AddCardPayloadDTO payloadDTO,
            Authentication authentication) {
        String email = "";
        email = authentication.getName();
        Optional<Wallet> optionalWallet = walletService.findById(payloadDTO.getWalletId());
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Wallet wallet;
        Account account;

        if (optionalAccount.isPresent() && optionalWallet.isPresent()) {
            wallet = optionalWallet.get();
            account = optionalAccount.get();
        } else {
            return ResponseEntity.badRequest().body("Account or Wallet does not exist!");
        }
        if (account.getId() != wallet.getAccount().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have such privilege");
        }

        try {
            Card card = new Card();
            card.setCard_number(payloadDTO.getCard_number());
            card.setCvv(payloadDTO.getCvv());
            card.setExpiry_date(payloadDTO.getExpiry_date());
            card.setWallet(wallet);
            cardService.save(card);

            return ResponseEntity.ok("Card added to wallet successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping(value = "/delete-card")
    @ApiResponse(responseCode = "200", description = "Card deleted")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Delete card from your wallet")
    @SecurityRequirement(name = "bulq-demo-api")
    public ResponseEntity<String> deleteCatalog(@RequestBody DeleteCardPayloadDTO payloadDTO,
            Authentication authentication) {
        String email = "";
        email = authentication.getName();
        Optional<Card> optionalCard = cardService.findById(payloadDTO.getCardId());
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Card card;
        Account account;

        if (optionalAccount.isPresent()) {
            account = optionalAccount.get();
            card = optionalCard.get();
        } else {
            return ResponseEntity.badRequest().body("Account or Wallet does not exist!");
        }
        if (card.getId() != card.getWallet().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have such privilege");
        }
        cardService.deleteById(card.getId());
        return ResponseEntity.badRequest().body("Card deleted successfully!");
    }

}
