package com.bulq.logistics.models;

import com.bulq.logistics.util.constants.ReadStatus;

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
public class Notification {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    private String image;

    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "verified_type", columnDefinition = "enum('READ', 'UNREAD') DEFAULT 'UNREAD'")
    private ReadStatus status;

    //Many to one relationship with user
    @ManyToOne
    @JoinColumn(name="account_id", referencedColumnName="id", nullable=true)
    private Account account;
}
