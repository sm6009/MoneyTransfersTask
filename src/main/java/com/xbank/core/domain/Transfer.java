package com.xbank.core.domain;

import com.xbank.core.dto.TransferResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Transfer {

    private UUID transferId;
    private Integer sourceAccount;
    private Integer targetAccount;
    private BigDecimal amount;
    private Currency currency;
    private String reference;

    public static TransferResponseDto toTransferResponseDto(Transfer transfer) {
        return new TransferResponseDto(transfer.transferId, "completed");
    }

}
