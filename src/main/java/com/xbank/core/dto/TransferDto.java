package com.xbank.core.dto;

import com.xbank.core.domain.Transfer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {
    private Integer fromAccount;
    private Integer toAccount;
    private BigDecimal amount;
    private Currency currency;
    private String reference;

    public static Transfer toTransfer(TransferDto transferDetails) {
        return new Transfer(UUID.randomUUID(), transferDetails.fromAccount, transferDetails.toAccount,
                transferDetails.getAmount(), transferDetails.getCurrency(), transferDetails.getReference());
    }
}
