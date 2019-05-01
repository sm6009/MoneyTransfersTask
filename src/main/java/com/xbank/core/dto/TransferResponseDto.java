package com.xbank.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TransferResponseDto {
    private UUID id;
    private String stateMessage;
}
