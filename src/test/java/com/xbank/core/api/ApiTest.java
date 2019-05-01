package com.xbank.core.api;

import com.xbank.core.domain.Account;
import com.xbank.core.dto.AccountDto;
import com.xbank.core.dto.TransferDto;
import com.xbank.core.injector.JerseyConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class ApiTest extends JerseyTest {

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new JerseyConfig();
    }

    @Test
    public void testFetchAccounts() {
        Response output = target("/accounts").request().get();
        List<Account> accounts = (List<Account>) output.readEntity(List.class);
        assertEquals(4,accounts.size());
    }

    @Test
    public void testCreateAccount() {
        AccountDto account = new AccountDto(130, "MIR", new BigDecimal(1000),
                Currency.getInstance("EUR"), LocalDateTime.now());
        Response output = target("/accounts").request().put(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(201,output.getStatus());
        output = target("/accounts").request().get();
        List<Account> accounts = (List<Account>) output.readEntity(List.class);
        assertEquals(5,accounts.size());
    }

    @Test
    public void testRemoveAccount() {
        Response output = target("/accounts/125").request().delete();
        assertEquals(200,output.getStatus());
        output = target("/accounts").request().get();
        List<Account> accounts = (List<Account>) output.readEntity(List.class);
        assertEquals(3,accounts.size());
    }

    @Test
    public void testSameCurrencyTransfer() {
        TransferDto transfer = new TransferDto(123,124,new BigDecimal(50),
                Currency.getInstance("EUR"), "test");
        Response output = target("/transfer").request().post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(200, output.getStatus());
        output = target("/accounts/123").request().get();
        AccountDto accountFrom = output.readEntity(AccountDto.class);
        assertEquals(new BigDecimal(50), accountFrom.getBalance());
        output = target("/accounts/124").request().get();
        AccountDto accountTo = output.readEntity(AccountDto.class);
        assertEquals(new BigDecimal(150), accountTo.getBalance());
    }

    @Test
    public void testCannotGoNegative() {
        TransferDto transfer = new TransferDto(123,124,new BigDecimal(200),
                Currency.getInstance("EUR"), "test");
        Response output = target("/transfer").request().post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(500, output.getStatus());
        output = target("/accounts/123").request().get();
        AccountDto accountFrom = output.readEntity(AccountDto.class);
        assertEquals(new BigDecimal(100), accountFrom.getBalance());
        output = target("/accounts/124").request().get();
        AccountDto accountTo = output.readEntity(AccountDto.class);
        assertEquals(new BigDecimal(100), accountTo.getBalance());
    }

    @Test
    public void testMultiThreadedTransfers() throws InterruptedException {
        int nrOfThreads = 100;
        ExecutorService WORKER_THREAD_POOL
                = Executors.newFixedThreadPool(nrOfThreads);
        CountDownLatch latch = new CountDownLatch(nrOfThreads);
        //submit multiple requests of A->B and B<-A transfers that should sum up to the original balance
        for (int i = 0; i < nrOfThreads; i++) {
            WORKER_THREAD_POOL.submit(() -> {
                TransferDto transfer = new TransferDto(123,124,new BigDecimal(1),
                        Currency.getInstance("EUR"), "test");
                Response output = target("/transfer").request().post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
                transfer = new TransferDto(124,123, new BigDecimal(1),
                        Currency.getInstance("EUR"), "test");
                output = target("/transfer").request().post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
                latch.countDown();
            });
        }
        latch.await();

        Response output = target("/accounts/123").request().get();
        AccountDto accountFrom = output.readEntity(AccountDto.class);
        assertEquals(new BigDecimal(100), accountFrom.getBalance());
        output = target("/accounts/124").request().get();
        AccountDto accountTo = output.readEntity(AccountDto.class);
        assertEquals(new BigDecimal(100), accountTo.getBalance());

    }
}
