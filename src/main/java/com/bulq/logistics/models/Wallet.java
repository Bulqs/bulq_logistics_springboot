package com.bulq.logistics.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Wallet {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private String walletName;

    private LocalDateTime createdAt;

    private BigDecimal balance = BigDecimal.ZERO;

    // Constructor
    public Wallet() {
        this.balance = BigDecimal.ZERO; // Ensures balance is set to zero
    }

    //one to one relationship with product
    @OneToOne
    @JoinColumn(name="account_id", referencedColumnName="id", nullable=true)
    private Account account;
}
