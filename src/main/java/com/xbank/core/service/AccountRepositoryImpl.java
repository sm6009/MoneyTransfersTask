package com.xbank.core.service;

import com.xbank.core.domain.Account;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class AccountRepositoryImpl implements AccountRepository {

    private final Map<Integer, Account> accountRepository = new ConcurrentHashMap<>();

    @Override
    public boolean add(Account account) {
        if (accountRepository.containsKey(account)){
            return false;
        } else {
            accountRepository.put(account.getAccountNumber(), account);
            return true;
        }
    }

    @Override
    public boolean remove(Account account) {
        return accountRepository.remove(account.getAccountNumber()) != null;
    }

    @Override
    public Collection<Account> extractAll() {
        return accountRepository.values();
    }

    @Override
    public Optional<Account> findByAccountNumber(Integer accountNumber) {
        return Optional.ofNullable(accountRepository.get(accountNumber));
    }
}
