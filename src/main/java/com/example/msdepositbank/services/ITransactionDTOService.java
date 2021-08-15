package com.example.msdepositbank.services;

import com.example.msdepositbank.models.dto.TransactionDTO;
import reactor.core.publisher.Mono;

public interface ITransactionDTOService {
    public Mono<TransactionDTO> saveTransaction(TransactionDTO transaction);
}
