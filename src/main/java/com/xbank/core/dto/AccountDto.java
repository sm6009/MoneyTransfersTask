package com.xbank.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.xbank.core.domain.Account;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    @Setter(AccessLevel.NONE)
    private Integer accountNumber;

    private String accountName;
    private BigDecimal balance;
    private Currency currency;

    @Setter(AccessLevel.NONE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyyMMdd HH:mm:ss")
    private LocalDateTime localDateTime;

    public static Account createAccount(AccountDto accountDto) {
        return new Account(accountDto.accountNumber, accountDto.accountName, accountDto.balance, accountDto.currency, LocalDateTime.now());
    }

}
