package com.xbank.core.injector;

import com.xbank.core.Main;
import com.xbank.core.service.AccountRepository;
import com.xbank.core.service.TransferRepository;
import com.xbank.core.service.TransferRepositoryImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class Binder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(Main.getAccountRepository()).to(AccountRepository.class);
        bind(TransferRepositoryImpl.class).to(TransferRepository.class);
    }
}
