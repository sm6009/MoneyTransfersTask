package com.xbank.core.service;

import com.xbank.core.domain.Account;
import com.xbank.core.domain.Transfer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Currency;

@Singleton
public class TransferRepositoryImpl implements TransferRepository {

    private AccountRepository accountRepository;

    @Inject
    public TransferRepositoryImpl(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean processTransfer(Transfer transfer) {
        Account fromAccount = accountRepository.findByAccountNumber(transfer.getSourceAccount())
                .orElseThrow(()
                        -> new IllegalArgumentException("Payer account " + transfer.getSourceAccount() + " not found !"));
        Account toAccount = accountRepository.findByAccountNumber(transfer.getTargetAccount())
                .orElseThrow(()
                        -> new IllegalArgumentException("Payee account " + transfer.getTargetAccount() + " not found !"));

        if (fromAccount == toAccount) throw new IllegalArgumentException("Cannot transfer to the same account!");

        Currency currency = transfer.getCurrency();

        if (fromAccount.getCurrency() != currency)
            throw new IllegalArgumentException("Cannot transfer " + currency + " from " + fromAccount.getCurrency()
                    + " account. Try a different account.");

        if (toAccount.getCurrency() != currency)
            throw new IllegalArgumentException("Cannot transfer " + currency + " to " + toAccount.getCurrency()
                    + " account. Try a different account.");

        executeThreadSafeTransaction(fromAccount, toAccount, transfer.getAmount());
        return true;
    }

    private void executeThreadSafeTransaction(Account fromAccount, Account toAccount, BigDecimal amount) {
        /*
            We need to ensure the locks are always acquired in the same order in order to avoid deadlocks
         */
        Account firstLock =  fromAccount;
        Account secondLock=  toAccount;
        if (fromAccount.getAccountNumber() > toAccount.getAccountNumber()) {
            firstLock = toAccount;
            secondLock = fromAccount;
        }
        synchronized (firstLock) {
            synchronized (secondLock) {
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    throw new IllegalArgumentException("Transfer declined. Reason: insufficient funds. Balance: "
                            + fromAccount.getBalance());
                }
                fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                toAccount.setBalance(toAccount.getBalance().add(amount));
            }
        }
    }
}
