package com.bulq.logistics.models;

import jakarta.persistence.Entity;
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
public class Card {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    private String card_number;

    private String expiry_date;

    private String cvv;

    //Many to one relationship with wallet
    @ManyToOne
    @JoinColumn(name="wallet_id", referencedColumnName="id", nullable=true)
    private Wallet wallet;
}
