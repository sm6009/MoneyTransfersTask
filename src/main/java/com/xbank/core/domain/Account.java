package com.xbank.core.domain;

import com.xbank.core.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;


@Data
@AllArgsConstructor
public class Account {

    private Integer accountNumber;
    private String accountName;
    private volatile BigDecimal balance;
    private Currency currency;
    private LocalDateTime localDateTime;

    public static AccountDto toDto(Account account) {
        return new AccountDto(account.accountNumber, account.accountName, account.balance, account.currency, account.localDateTime);
    }

}
