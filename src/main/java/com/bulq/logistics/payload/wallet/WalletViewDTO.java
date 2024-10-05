package com.bulq.logistics.payload.wallet;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletViewDTO {
    
    private long id;

    private String walletName;

    private BigDecimal balance;
}
