package com.xbank.core;

import com.xbank.core.domain.Account;
import com.xbank.core.service.AccountRepository;
import com.xbank.core.service.AccountRepositoryImpl;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public class Main {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8000);

        ServletContextHandler ctx =
                new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

        ctx.setContextPath("/");
        server.setHandler(ctx);

        ServletHolder serHol = ctx.addServlet(ServletContainer.class, "/api/*");
        serHol.setInitOrder(1);
        serHol.setInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS,
                "com.xbank.core.injector.JerseyConfig");


        server.start();
        server.join();
    }

    public static AccountRepository getAccountRepository() {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        accountRepository.add(new Account(123, "EUR_MACRON", new BigDecimal(100), Currency.getInstance("EUR"), LocalDateTime.now()));
        accountRepository.add(new Account(124, "EUR_MERKEL", new BigDecimal(100), Currency.getInstance("EUR"), LocalDateTime.now()));
        accountRepository.add(new Account(125, "USD_TRUMP", new BigDecimal(100), Currency.getInstance("USD"), LocalDateTime.now()));
        accountRepository.add(new Account(126, "USD_CLINTON", new BigDecimal(100), Currency.getInstance("USD"), LocalDateTime.now()));
        return accountRepository;
    }
}
