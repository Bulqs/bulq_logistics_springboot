package com.bulq.logistics.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.bulq.logistics.util.constants.Verification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    @Column(unique=true)
    private String email;

    private String password;

    private String authorities;

    private String image;

    private LocalDateTime createdAt;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String username;

    private String mandateDocument;

    private String isKycCompleted;

    private String country;

    private String state;

    private String city;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "verified", columnDefinition = "enum('VERIFIED', 'PENDING', 'REJECTED') DEFAULT 'PENDING'")
    private Verification verified;

    private String otp;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Column(name = "token")
    private String token;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    
    @CreatedDate
    private LocalDateTime createdDate;

    private LocalDateTime passswordResetTokenExpiry;

    // One-to-one relationship with Wallet (mapped by the 'account' field in Wallet)
    @OneToOne(mappedBy = "account")
    private Wallet wallet;
    
}
