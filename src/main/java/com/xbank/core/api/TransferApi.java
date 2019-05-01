package com.xbank.core.api;

import com.xbank.core.dto.TransferDto;
import com.xbank.core.dto.TransferResponseDto;
import com.xbank.core.domain.Transfer;
import com.xbank.core.service.TransferRepository;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
public class TransferApi {

    @Inject
    private TransferRepository transferRepository;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public TransferResponseDto processTransfer(TransferDto transferDetails) {
        if (transferDetails == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Transfer transfer = TransferDto.toTransfer(transferDetails);
        transferRepository.processTransfer(transfer);
        return Transfer.toTransferResponseDto(transfer);
    }

}
