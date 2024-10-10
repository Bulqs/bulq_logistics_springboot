package com.bulq.logistics.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

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

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    
    @CreatedDate
    private LocalDateTime createdDate;

    // Constructor
    public Wallet() {
        this.balance = BigDecimal.ZERO; // Ensures balance is set to zero
    }

    //one to one relationship with product
    @OneToOne
    @JoinColumn(name="account_id", referencedColumnName="id", nullable=true)
    private Account account;
}
