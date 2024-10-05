package com.bulq.logistics.payload.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.transaction.TransactionStatus;

import com.bulq.logistics.util.constants.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionViewDTO {

    private long id;

    private LocalDateTime transactionDate;

    private String transactionType;

    private BigDecimal amount;

    private String recipient;

    private String transactionStatus;
}

