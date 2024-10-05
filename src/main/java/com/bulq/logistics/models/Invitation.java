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
public class Invitation {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    private String userStatus; //"PENDING", "ACCEPTED", "DECLINED"

    private Integer amount;

    private LocalDateTime inviteDate;

    private Integer earn;

    private LocalDateTime responseDate;

    private String invitedUserId;

    private String invitedUserName;

    //Many to one relationship with user
}
