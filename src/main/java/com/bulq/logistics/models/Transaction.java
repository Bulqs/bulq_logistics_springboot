package com.bulq.logistics.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bulq.logistics.util.constants.TransactionStatus;
import com.bulq.logistics.util.constants.TransactionType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", columnDefinition = "enum('NEWWALLET','FUND', 'WITHDRAW') DEFAULT 'NEWWALLET'")
    private TransactionType transactionType; //fund, withdraw

    private BigDecimal amount;

    private String recipient; // who did the transaction

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", columnDefinition = "enum('NEW','COMPLETED', 'PENDING', 'CANCELLED') DEFAULT 'PENDING'")
    private TransactionStatus transactionStatus; // 'COMPLETED', 'PENDING', 'CANCELLED'

    //Many to one relationship with wallet
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="wallet_id", referencedColumnName="id", nullable=true)
    private Wallet wallet;

    //one to one relationship with product
    @OneToOne
    @JoinColumn(name="booking_id", referencedColumnName="id", nullable=true)
    private Booking booking;
}
