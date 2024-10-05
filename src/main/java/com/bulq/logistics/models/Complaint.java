package com.bulq.logistics.models;

import java.time.LocalDateTime;

import com.bulq.logistics.util.constants.ComplaintType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Complaint {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    private String complaint;

    private String complainant; //Name of complainant.

    private String delivery_code;

    @Enumerated(EnumType.STRING)
    @Column(name = "complaint_status", columnDefinition = "enum('OPEN', 'PENDING', 'RESOLVED', 'CLOSED') DEFAULT 'PENDING'")
    private ComplaintType complaint_status; 

    private LocalDateTime createdAt;

    private String resolution;

    // private String notes;

    //Many to one relationship with user
    @ManyToOne
    @JoinColumn(name="account_id", referencedColumnName="id", nullable=true)
    private Account account;
}
