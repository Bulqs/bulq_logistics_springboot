package com.bulq.logistics.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class KYCVerification {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    private String nin;

    private String bvn;

    private String mandateDocument;

    private LocalDateTime submissionDate;

    private LocalDateTime isNinVerified;

    private LocalDateTime isBvnVerified;

    private LocalDateTime isFullyVerified;

    //one to one relationship with user
}
