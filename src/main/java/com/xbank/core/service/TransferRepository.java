package com.xbank.core.service;

import com.xbank.core.domain.Transfer;

public interface TransferRepository {

    public boolean processTransfer(Transfer transfer);
}
