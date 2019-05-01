package com.xbank.core;

import com.xbank.core.domain.Account;
import com.xbank.core.domain.Transfer;
import com.xbank.core.service.AccountRepository;
import com.xbank.core.service.AccountRepositoryImpl;
import com.xbank.core.service.TransferRepository;
import com.xbank.core.service.TransferRepositoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TransferTest {

    private AccountRepository accountRepository;
    private TransferRepository transferRepository;

    @Before
    public void init(){
        populateAccountRepository();
        transferRepository = new TransferRepositoryImpl(accountRepository);
    }

    private void populateAccountRepository() {
        accountRepository = new AccountRepositoryImpl();
        accountRepository.add(new Account(123, "EUR_MACRON", new BigDecimal(100),
                Currency.getInstance("EUR"), LocalDateTime.now()));
        accountRepository.add(new Account(124, "EUR_MERKEL", new BigDecimal(100),
                Currency.getInstance("EUR"), LocalDateTime.now()));
        accountRepository.add(new Account(125, "USD_TRUMP", new BigDecimal(100),
                Currency.getInstance("USD"), LocalDateTime.now()));
        accountRepository.add(new Account(126, "USD_CLINTON", new BigDecimal(100),
                Currency.getInstance("USD"), LocalDateTime.now()));
    }


    @Test
    public void testSameCurrencyTransfer(){
        Transfer transfer = new Transfer(UUID.randomUUID(),123,124,new BigDecimal(50),
                Currency.getInstance("EUR"), "test");
        transferRepository.processTransfer(transfer);
        assertEquals(accountRepository.findByAccountNumber(123).get().getBalance(), new BigDecimal(50));
        assertEquals(accountRepository.findByAccountNumber(124).get().getBalance(), new BigDecimal(150));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotGoNegativeBalance(){
        Transfer transfer = new Transfer(UUID.randomUUID(),123,124,new BigDecimal(150),
                Currency.getInstance("EUR"), "test");
        transferRepository.processTransfer(transfer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotTransferToDifferentCcyAccount(){
        Transfer transfer = new Transfer(UUID.randomUUID(),123,125,new BigDecimal(50),
                Currency.getInstance("EUR"), "test");
        transferRepository.processTransfer(transfer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotTransferTomyself(){
        Transfer transfer = new Transfer(UUID.randomUUID(),123,123,new BigDecimal(50),
                Currency.getInstance("EUR"), "test");
        transferRepository.processTransfer(transfer);
    }

    @Test
    public void testCreateAccount(){
        Account account = new Account(130, "MIR", new BigDecimal(1000),
                Currency.getInstance("GBP"), LocalDateTime.now());
        accountRepository.add(account);
        assertEquals("MIR", accountRepository.findByAccountNumber(130).get().getAccountName());
    }



}
