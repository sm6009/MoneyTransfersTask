package com.xbank.core.api;

import com.xbank.core.dto.AccountDto;
import com.xbank.core.domain.Account;
import com.xbank.core.service.AccountRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Path("/accounts")
public class AccountApi {

    @Inject
    private AccountRepository accountRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountDto> retrieveAccounts() {
        return accountRepository.extractAll().stream().map(Account::toDto).collect(toList());
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAccount(AccountDto accountDto) {
        boolean created = accountRepository.add(AccountDto.createAccount(accountDto));
        if (created) return Response.status(Response.Status.CREATED).build();
        else return Response.status(Response.Status.CONFLICT).build();
    }

    @DELETE
    @Path("{accountNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAccount(@PathParam("accountNumber") Integer accountNumber) {
        Optional<Account> accountObj = accountRepository.findByAccountNumber(accountNumber);
        if (!accountObj.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        accountRepository.remove(accountObj.get());
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDto retrieveAccounts(@PathParam("accountNumber") Integer accountNumber) {
        Optional<AccountDto> responseObj = accountRepository.findByAccountNumber(accountNumber).map(Account::toDto);
        if (!responseObj.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return responseObj.get();
    }
}
