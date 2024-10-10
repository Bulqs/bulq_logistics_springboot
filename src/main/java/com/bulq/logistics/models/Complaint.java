package com.bulq.logistics.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

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

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    
    @CreatedDate
    private LocalDateTime createdDate;

    // private String notes;

    //Many to one relationship with user
    @ManyToOne
    @JoinColumn(name="account_id", referencedColumnName="id", nullable=true)
    private Account account;
}
