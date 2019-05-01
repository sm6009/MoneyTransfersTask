package com.xbank.core.service;

import com.xbank.core.domain.Account;

import java.util.Collection;
import java.util.Optional;

public interface AccountRepository {

    boolean add(Account account);
    boolean remove(Account account);
    Collection<Account> extractAll();
    Optional<Account> findByAccountNumber(Integer accountNumber);

}
